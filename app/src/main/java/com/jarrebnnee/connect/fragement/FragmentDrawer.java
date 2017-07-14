package com.jarrebnnee.connect.fragement;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.jarrebnnee.connect.Adapter.NavigationDrawerAdapter;
import com.jarrebnnee.connect.LocationUpdaterService;
import com.jarrebnnee.connect.MainActivity;
import com.jarrebnnee.connect.Manifest;
import com.jarrebnnee.connect.NavDrawerItem;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerServiceSetActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.SimpleDividerItemDecoration;
import com.jarrebnnee.connect.Urlcollection;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jigna on 13/10/16.
 */
public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    TextView tv_welusername;
    ImageView iv1;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    String country;
    SaveSharedPrefrence sharedPreferences;
    String u_first_name, u_last_name, u_image;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> map;
    String lang;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        sharedPreferences = new SaveSharedPrefrence();
        lang = sharedPreferences.getlang(getApplicationContext());
        u_first_name = sharedPreferences.getUserFName(getApplicationContext());
        u_last_name = sharedPreferences.getUserLName(getApplicationContext());
        u_image = sharedPreferences.getUserImage(getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        tv_welusername = (TextView) layout.findViewById(R.id.tv_welusername);
        iv1 = (ImageView) layout.findViewById(R.id.iv1);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/16023_futuran_0.ttf");
        tv_welusername.setTypeface(custom_font);
        new MainCategory(getActivity()).execute();

        adapter = new NavigationDrawerAdapter(getActivity(), getData(), list);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                if (position == 3) {
                    Intent i = new Intent(getApplicationContext(), SellerServiceSetActivity.class);
                    startActivity(i);
                } else if (position == 4) {
                    CustomDialog();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        tv_welusername.setText(R.string.welcome + "\n" + u_first_name.toString() + " " + u_last_name.toString());
        if (u_image == null||u_image=="") {

        } else {
            Picasso.with(getApplicationContext()).load(u_image).into(iv1);
        }

        return layout;

    }

    private void CustomDialog() {

        Button dialogButtonno, dialogButtonyes;
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_layout_logout);
        dialog.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        dialog.setTitle(getResources().getString(R.string.logout));

        dialogButtonno = (Button) dialog.findViewById(R.id.btnno);
        dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);

        dialog.show();

        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                sharedPreferences.saveUserID(getActivity(), "");

                String id = sharedPreferences.getUserID(getActivity());
               /* SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                settings.edit().remove("KeyName").commit();*/
                LoginManager.getInstance().logOut();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                if (isMyServiceRunning(LocationUpdaterService.class)) {
                    Log.e("service", "stopping");
                    Intent intent1 = new Intent(getActivity(), LocationUpdaterService.class);
                    getActivity().stopService(intent1);
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.logoutsucess), Toast.LENGTH_SHORT).show();
                getActivity().finish();
                dialog.dismiss();

            }
        });

        dialogButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                dialog.dismiss();

            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    class MainCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        MainCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            list = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "category_list"));
            nameValuePairs.add(new BasicNameValuePair("lang", lang));

            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);
            Log.d("nameValuePairs: ", "> " + nameValuePairs);
            Log.d("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");

                        map = new HashMap<String, String>();
                        map.put("c_id", c_id);
                        map.put("c_images", c_images);
                        map.put("c_type", c_type);
                        map.put("c_title", c_title);
                        map.put("c_is_parent_id", c_is_parent_id);
                        map.put("c_total_services", c_total_services);
                        map.put("c_created", c_created);
                        map.put("s_modified", s_modified);
                        list.add(map);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // pDialog.dismiss();
        }
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = new SaveSharedPrefrence();
        u_first_name = sharedPreferences.getUserFName(getApplicationContext());
        u_last_name = sharedPreferences.getUserLName(getApplicationContext());
        u_image = sharedPreferences.getUserImage(getApplicationContext());
        tv_welusername.setText("Welcome \n" + u_first_name + " " + u_last_name);
        if (u_image == null||u_image=="") {

        } else {
            Picasso.with(getApplicationContext()).load(u_image).into(iv1);
        }
        adapter = new NavigationDrawerAdapter(getActivity(), getData(), list);
        recyclerView.setAdapter(adapter);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);


    }
}
