package com.jarrebnnee.connect.fragement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.BookCateListAdapter;
import com.jarrebnnee.connect.Adapter.BookCityListAdapter;
import com.jarrebnnee.connect.Adapter.BookCountryListAdapter;
import com.jarrebnnee.connect.Adapter.SpinnerAdapter;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class BookNow_Fragement extends Fragment {

    EditText edJTitle,edJDesc,edPrice;
    Spinner spService,spRadius;//,spCountry,spCity;
    TextView edDate,edJPic;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    SpinnerAdapter adapterCountry;
    ArrayAdapter<String> adapterService;
    String[] country={"Malaysia","United States","Indonesia","France","India"};
    String[] service={"Electrician","Plumber"};

    ArrayList<HashMap<String,String>> countrylist;
    HashMap<String,String> cmap;
    ArrayList<HashMap<String,String>> citylist;
    HashMap<String,String> citymap;
    ArrayList<HashMap<String,String>> cat_list;
    ArrayList<HashMap<String,String>> subcat_list;
    HashMap<String,String> map;

    BookCountryListAdapter countryListAdapter;
    BookCityListAdapter cityListAdapter;
    BookCateListAdapter bookCateListAdapter;
    String country_id;
    private static int RESULT_LOAD_IMG = 1;
    Button btn_submit;
    String action = "job_service",js_user_id,js_category_id="",js_title,js_description,js_radius,js_date,js_price,u_type;
    SaveSharedPrefrence saveSharedPrefrence;
    private String imagepath=null;
    String imgDecodableString,js_cate_id="0";
    private int serverResponseCode = 0;
    RelativeLayout rvRadius,rvService;
    LinearLayout lyPrice,lyDate,lyJDesc,lyJTitle,lyJPic;
    Spinner spsubService;
    RelativeLayout rvsubService;
    ArrayList<String> cate_id;
    String lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_book_now, container, false);
        saveSharedPrefrence = new SaveSharedPrefrence();
        js_user_id = saveSharedPrefrence.getUserID(getActivity());
        countrylist = new ArrayList<HashMap<String,String>>();
        citylist = new ArrayList<HashMap<String,String>>();
        cat_list = new ArrayList<HashMap<String,String>>();
        u_type = saveSharedPrefrence.getUserType(getActivity());
        cate_id = new ArrayList<String>();
        lang = saveSharedPrefrence.getlang(getActivity());

        lyPrice = (LinearLayout)rootView.findViewById(R.id.lyPrice);
        lyDate = (LinearLayout)rootView.findViewById(R.id.lyDate);
        lyJDesc = (LinearLayout)rootView.findViewById(R.id.lyJDesc);
        lyJTitle = (LinearLayout)rootView.findViewById(R.id.lyJTitle);
        lyJPic = (LinearLayout)rootView.findViewById(R.id.lyJPic);

        rvService = (RelativeLayout)rootView.findViewById(R.id.rvService);
        rvRadius = (RelativeLayout)rootView.findViewById(R.id.rvRadius);
        rvsubService = (RelativeLayout)rootView.findViewById(R.id.rvsubService);

        edJTitle = (EditText)rootView.findViewById(R.id.edJTitle);
        edJDesc = (EditText)rootView.findViewById(R.id.edJDesc);
        edPrice = (EditText)rootView.findViewById(R.id.edPrice);
        spRadius = (Spinner) rootView.findViewById(R.id.spRadius);

        spService = (Spinner)rootView.findViewById(R.id.spService);
        spsubService = (Spinner)rootView.findViewById(R.id.spsubService);

       // spCountry = (Spinner)rootView.findViewById(R.id.spCountry);

        edDate = (TextView)rootView.findViewById(R.id.edDate);
        edJPic = (TextView)rootView.findViewById(R.id.edJPic);
        btn_submit = (Button)rootView.findViewById(R.id.btn_submit);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/16023_futuran_0.ttf");
        edDate.setTypeface(custom_font);
        edJTitle.setTypeface(custom_font);
        edJDesc.setTypeface(custom_font);
        edPrice.setTypeface(custom_font);
        edJPic.setTypeface(custom_font);
        btn_submit.setTypeface(custom_font);
        if (u_type.equals("2")){
            lyPrice.setVisibility(View.GONE);
            lyDate.setVisibility(View.GONE);
            lyJDesc.setVisibility(View.GONE);
            lyJTitle.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            lyJPic.setVisibility(View.GONE);
            rvService.setVisibility(View.GONE);
            rvRadius.setVisibility(View.GONE);

        }else{
            lyDate.setVisibility(View.VISIBLE);
            lyJTitle.setVisibility(View.VISIBLE);
            lyJDesc.setVisibility(View.VISIBLE);
            lyPrice.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            lyJPic.setVisibility(View.VISIBLE);
            rvService.setVisibility(View.VISIBLE);
            rvRadius.setVisibility(View.VISIBLE);
        }

       adapterCountry =new SpinnerAdapter(
                getContext(),
                R.layout.view_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.radius_arrays))
        );
        spRadius.setAdapter(adapterCountry);

        edJPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });


       /* spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = countrylist.get(position);
                country_id = map.get("id");

                new City(getActivity()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = citylist.get(position);
               // city_id = map.get("city_id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);
            new MainCategory(getActivity()).execute();

        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = cat_list.get(position);
                js_cate_id= map.get("c_id");
                String c_is_sub_category = map.get("c_is_sub_category");

                if(js_cate_id.equals("0")){

                }else{
                    cate_id.add(js_cate_id);
                   if (c_is_sub_category.equals("1")){
                        rvsubService.setVisibility(View.VISIBLE);
                        new SubCategory(getActivity()).execute();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spsubService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = subcat_list.get(position);
                String js_category_id = map.get("c_id");

                if(js_category_id.equals("0")){

                }else {
                   // cate_id.add(js_category_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        lyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                js_title = edJTitle.getText().toString();
                js_description = edJDesc.getText().toString();
                js_radius = spRadius.getSelectedItem().toString();
                js_date = edDate.getText().toString();
                js_price = edPrice.getText().toString();
               /* for (int i = 0; i < cate_id.size(); i++) {
                    js_category_id += cate_id.get(i);
                    js_category_id += ", ";
                    Log.e("s_category_id",""+js_category_id);
                }*/
                js_category_id=js_cate_id;

                if( edJTitle.getText().toString().length() == 0 ) {
                    edJTitle.setError(getResources().getString(R.string.jtitlerequired));
                }else if( edJDesc.getText().toString().length() == 0 ) {
                    edJDesc.setError(getResources().getString(R.string.jdescrequired));
                }else if(js_category_id.equals("")) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.srequired),Toast.LENGTH_LONG).show();
                }else if( js_date.equals("Appointment Date")) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.adaterequired),Toast.LENGTH_LONG).show();
                }else if( edPrice.getText().toString().length() == 0 ) {
                    edPrice.setError(getResources().getString(R.string.prequired));
                }else if( js_radius.equals("Select Radius") ) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.rrequired),Toast.LENGTH_LONG).show();
                }else {
                    if (imagepath == null){
                        new JobService(getActivity()).execute();
                    }else {
                        new JobService1(imagepath, js_user_id, js_category_id, js_title, js_description, js_radius, js_price, js_date,lang).execute();
                    }
                    new Notification(getActivity()).execute();
                    cate_id.clear();
                   // new JobService(getActivity()).execute();
                }
            }
        });




        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

      if (requestCode == RESULT_LOAD_IMG && responseCode == RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            imagepath = getPath(selectedImage);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Log.e("imgDecodableString",""+imgDecodableString);
            String fileName = imgDecodableString.substring(imgDecodableString.lastIndexOf('/') + 1);
            edJPic.setText(fileName);




        /*    try {
                byte[] str = imgDecodableString.getBytes("UTF-8");
                u_img = Base64.encodeToString(str, Base64.DEFAULT);
                Log.e("base64",""+u_img);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            // Set the Image in ImageView after decoding the String
            // imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

        } else {
           /* Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();*/
        }

    }
    class JobService1 extends AsyncTask<Void, Void, Void> {

        String sourceFileUri, js_user_id, js_category_id, js_title, js_description, js_radius, js_price, js_date,lang;
        ProgressDialog pDialog;
        public JobService1(String imagepath, String js_user_id, String js_category_id, String js_title, String js_description, String js_radius, String js_price, String js_date,String lang) {
            this.sourceFileUri = imagepath;
            this.js_user_id = js_user_id;
            this.js_category_id = js_category_id;
            this.js_title = js_title;
            this.js_description = js_description;
            this.js_radius = js_radius;
            this.js_price = js_price;
            this.js_date = js_date;
            this.lang = lang;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {

            String fileName = sourceFileUri;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (!sourceFile.isFile()) {

          /*  if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }*/

                Log.e("uploadFile", "Source File not exist :" + sourceFileUri);


            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL uri = new URL("http://jarbni.com/connect/ebay_clone_api/?action=postBookNow&js_user_id=" + js_user_id + "&js_category_id=" + js_category_id + "&js_title=" + js_title + "&js_description=" + js_description + "&js_radius=" + js_radius + "&js_price=" + js_price + "&js_appointment_date=" + js_date +"&lang=" +lang);
                    URL url = new URL(uri.toString().replace(" ", "%20"));
                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    Log.e("Content-Type", boundary);
                    conn.setRequestProperty("uploaded_file", sourceFile.getAbsolutePath());

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + sourceFile.getAbsolutePath() + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    Log.e("url", String.valueOf(url));

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println("Response : -- " + response.toString());
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String value = jsonResponse.getString("status");
                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                    ex.printStackTrace();

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                    e.printStackTrace();


                }

            /*if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();

            }*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            Toast.makeText(getActivity(), getResources().getString(R.string.jbsucess), Toast.LENGTH_LONG).show();
               /* Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();*/
            Home_Fragement fragemebt_home = new Home_Fragement();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragemebt_home);
            fragmentTransaction.commit();
        }

         // End else block
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edDate.setText(sdf.format(myCalendar.getTime()));
    }
    class Notification extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,text,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified,c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        Notification(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
          //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub


            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "sendNotification"));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    text = jsonObj.getString("text");
                    Log.e("text", text);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
    class JobService extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,text,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified,c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        JobService(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
              pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub


            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "postBookNow"));
            nameValuePairs.add(new BasicNameValuePair("js_user_id", js_user_id));
            nameValuePairs.add(new BasicNameValuePair("js_category_id", js_category_id));
            nameValuePairs.add(new BasicNameValuePair("js_title", js_title));
            nameValuePairs.add(new BasicNameValuePair("js_description", js_description));
            nameValuePairs.add(new BasicNameValuePair("js_price", js_price));
            nameValuePairs.add(new BasicNameValuePair("js_appointment_date", js_date));
            nameValuePairs.add(new BasicNameValuePair("js_radius", js_radius));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (status.equals("0")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.jbsucess), Toast.LENGTH_LONG).show();
                Home_Fragement fragemebt_home = new Home_Fragement();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_home);
                fragmentTransaction.commit();
            }

        }
    }
    class MainCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,c_ar_title,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified,c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        MainCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            cat_list = new ArrayList<HashMap<String, String>>();
            map = new HashMap<String, String>();
            String c_id = "0";
            c_is_sub_category = "0";
            map.put("c_id",c_id);
            map.put("c_images",c_images);
            map.put("c_type",c_type);
            map.put("c_title","Select Service");
            map.put("c_is_parent_id",c_is_parent_id);
            map.put("c_total_services",c_total_services);
            map.put("c_created",c_created);
            map.put("c_ar_title","حدد الخدمة");
            map.put("s_modified",s_modified);
            map.put("c_is_sub_category",c_is_sub_category);
            cat_list.add(map);

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMainCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        c_ar_title = object.getString("c_ar_title");
                        s_modified = object.getString("s_modified");
                        c_is_sub_category = object.getString("c_is_sub_category");

                        map = new HashMap<String, String>();
                        map.put("c_id",c_id);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_title",c_title);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);
                        map.put("c_created",c_created);
                        map.put("c_ar_title",c_ar_title);
                        map.put("s_modified",s_modified);
                        map.put("c_is_sub_category",c_is_sub_category);
                        cat_list.add(map);
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

            pDialog.dismiss();
            bookCateListAdapter  = new BookCateListAdapter(getActivity(),cat_list);
            spService.setAdapter(bookCateListAdapter);
        }
    }
    class SubCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified,c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        SubCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            subcat_list = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();
            String c_id = "0";
            c_is_sub_category = "0";

            map.put("c_id",c_id);
            map.put("c_images",c_images);
            map.put("c_type",c_type);
            map.put("c_title","Select Sub Service");
            map.put("c_is_parent_id",c_is_parent_id);
            map.put("c_total_services",c_total_services);
            map.put("c_created",c_created);
            map.put("s_modified",s_modified);
            map.put("c_is_sub_category",c_is_sub_category);
            subcat_list.add(map);

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getSubCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("c_id", js_cate_id));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++) {
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
                        subcat_list.add(map);

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

            pDialog.dismiss();
            bookCateListAdapter  = new BookCateListAdapter(getActivity(),subcat_list);
            spsubService.setAdapter(bookCateListAdapter);
        }
    }
}
