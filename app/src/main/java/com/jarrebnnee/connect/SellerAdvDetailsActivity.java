package com.jarrebnnee.connect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Service.Config;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class SellerAdvDetailsActivity extends AppCompatActivity {

    private static final String POST_FIELD = ""+10;
    Toolbar toolbar;
    ImageView ivBack, iv_img;
    TextView tvTitle, tv1, tv2, tv3, tv4, tv_selected_location;
    EditText et_title, et_desc;
    Spinner sp_categories;
    Button btn_cancel, btn_submit,tv_upload;
    TrackGPS gps;
    SaveSharedPrefrence saveSharedPrefrence;
    String u_id, lang;
    ArrayList<HashMap<String, String>> list;
    CountryListAdapter2 listAdapter;
    Bundle bundle;
    String locationName, c_id = "",  pkg_id, latitide, longitud,imgDecodableString;
    private static int RESULT_LOAD_IMG = 1;
    private String imagepath=null;
    String filePath;
    public static String BASE_URL = Urlcollection.url + "action=addAdvertise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_adv_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bundle = getIntent().getExtras();

        gps = new TrackGPS(SellerAdvDetailsActivity.this);
        saveSharedPrefrence = new SaveSharedPrefrence();

        locationName = bundle.getString("locationName");
        latitide = bundle.getString("lat");
        longitud = bundle.getString("lng");

        u_id = saveSharedPrefrence.getUserID(SellerAdvDetailsActivity.this);
        lang = saveSharedPrefrence.getlang(SellerAdvDetailsActivity.this);
        pkg_id=saveSharedPrefrence.getpkg_id(SellerAdvDetailsActivity.this);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv_upload = (Button) findViewById(R.id.tv_upload);
        tv_selected_location = (TextView) findViewById(R.id.tv_selected_location);
        et_desc = (EditText) findViewById(R.id.et_desc);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        et_title = (EditText) findViewById(R.id.et_title);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_details);
        btn_submit = (Button) findViewById(R.id.btn_submit_details);
        sp_categories = (Spinner) findViewById(R.id.sp_categories);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        tv3.setTypeface(custom_font);
        tv4.setTypeface(custom_font);
        tv_selected_location.setTypeface(custom_font);
        tv_upload.setTypeface(custom_font);
        et_desc.setTypeface(custom_font);
        et_title.setTypeface(custom_font);
        btn_submit.setTypeface(custom_font);
        btn_cancel.setTypeface(custom_font);


        tv_selected_location.setText(locationName);


        list = new ArrayList<HashMap<String, String>>();
        listAdapter = new CountryListAdapter2(SellerAdvDetailsActivity.this, list);
        sp_categories.setAdapter(listAdapter);
        sp_categories.setPrompt("Select Category");

        sp_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> cmap1 = list.get(position);
                String title = cmap1.get("c_title");
                if (title.equals("Select a Category")) {
                    c_id = "";
                } else {
                  //  Toast.makeText(SellerAdvDetailsActivity.this, "Name " + cmap1.get("c_title") + "\nID " + cmap1.get("c_id"), Toast.LENGTH_LONG).show();
                    c_id = cmap1.get("c_id");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp1 = et_title.getText().toString().trim();


                String temp2 = et_desc.getText().toString().trim();

                if (temp1.length() <= 0) {
                    et_title.setError("Title cannot be blank");
                    return;
                } else if (temp2.length() <= 0) {
                    et_desc.setError("Description cannot be blank");
                    return;
                } else if (c_id.equals("")) {
                    Toast.makeText(SellerAdvDetailsActivity.this, "Please select a category to proceed", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String title = temp1.replace(" ", "%20");
                    String desc = temp2.replace(" ", "%20");
                //    Toast.makeText(SellerAdvDetailsActivity.this, "Ready to go!!!!", Toast.LENGTH_SHORT).show();
                    Log.e("intent_", "lat : "+latitide+"\nlng : "+longitud);
                    if (filePath != null) {
                       // imageUpload(filePath);
                        new UploadFileToServer(latitide, longitud, temp1, temp2).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                    }
                  //  CallAddAdvertiseApi(title,desc);
                }
            }
        });


        CallSellerServiceApi();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {


            Uri picUri = data.getData();

            filePath = getPath(picUri);
            Log.e("picUri", picUri.toString());
            Log.e("filePath", filePath);

            File f= new File(filePath);
            iv_img.setVisibility(View.VISIBLE);
            Picasso.with(SellerAdvDetailsActivity.this).load(f).into(iv_img);

            // Get the Image from data
           /* Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            File file = null;
            try {
                file = new File(new URI(uri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            final Handler handler = new Handler();
            MediaScannerConnection.scanFile(
                    this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, final Uri uri) {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        uploadFile(uri);
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });*/

            /*Uri selectedImage = data.getData();
            imagepath = getPath(selectedImage);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Log.e("imgDecodableString",""+imgDecodableString);
            String fileName = imgDecodableString.substring(imgDecodableString.lastIndexOf('/') + 1);
            tv_upload.setText(fileName);*/

            // new UploadFileAsync(imgDecodableString).execute();


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

        }
    }

    /*private void imageUpload(final String imagePath) {
        final ProgressDialog pDialog = new ProgressDialog(SellerAdvDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            pDialog.hide();
                            JSONObject jObj = new JSONObject(response);

                            String message = jObj.getString("message");

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addFile("uploaded_file", imagePath);
        smr.addFile("lang", lang);
        smr.addFile("u_id", u_id);
        smr.addFile("latitude", latitide);
        smr.addFile("longitude", longitud);
        smr.addFile("title", et_title.getText().toString());
        smr.addFile("description", et_desc.getText().toString());
        smr.addFile("pkg_id", pkg_id);

        MyApplication.getInstance().addToRequestQueue(smr);

    }*/

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    /*public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

  /*  private String uploadFile(Uri resourceUri) throws URISyntaxException {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        final File sourceFile = new File(new URI(resourceUri.toString()));
        String serverResponseMessage = null;
        String responce = null;
        if (!sourceFile.isFile()) {

            //dialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "File not found !", Toast.LENGTH_LONG).show();
                }
            });

            return "no file";
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath());
                URL url = new URL("http://jarbni.com/connect/uploads/advertise");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty(POST_FIELD, sourceFile.getName());
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + POST_FIELD + "\";filename="
                        + sourceFile.getName() + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                if (serverResponseCode <= 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(SellerAdvDetailsActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (MalformedURLException ex) {
                //dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(SellerAdvDetailsActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
             //   dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SellerAdvDetailsActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
        }
       // dialog.dismiss();
        return responce;
    }*/

    private void CallAddAdvertiseApi(String title, String desc) {

        String url = Urlcollection.url + "action=addAdvertise&lang=" + lang + "&u_id=" + u_id+"&c_id="+c_id+"&latitude="+latitide+"&longitude="+longitud+"&title="+title+"&description="+desc+"&pkg_id="+pkg_id;
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(SellerAdvDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    Toast.makeText(SellerAdvDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
                MyApplication.getInstance().addToRequestQueue(request);
    }

    private void CallSellerServiceApi() {

        String url = Urlcollection.url + "action=getMainCategoryList&lang=" + lang + "&u_id=" + u_id;
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(SellerAdvDetailsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();

                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    String message = jsonObj.getString("message");
                    HashMap<String, String> map1 = new HashMap<String, String>();
                    map1.put("c_id", "" + -1);
                    map1.put("c_title", "Select a Category");
                    list.add(map1);
                    Log.e("added", "" + list.size());
                    listAdapter.notifyDataSetChanged();
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        String c_id = object.getString("c_id");
                        String c_title = object.getString("c_title");
                        String c_ar_title = object.getString("c_ar_title");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("c_id", c_id);
                        map.put("c_title", c_title);
                        map.put("c_ar_title", c_ar_title);
                        list.add(map);
                        Log.e("added", "" + list.size());
                        listAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        MyApplication.getInstance().addToRequestQueue(request);
    }


    public class CountryListAdapter2 extends BaseAdapter {

        Context c;
        ArrayList<HashMap<String, String>> cityList;
        HashMap<String, String> resultp;
        LayoutInflater inflater;


        public CountryListAdapter2(Context activity,
                                   ArrayList<HashMap<String, String>> stateList) {
            this.c = activity;
            this.cityList = stateList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cityList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv_cate;
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.custom_spinner, parent, false);

            resultp = new HashMap<String, String>();
            resultp = cityList.get(position);

            tv_cate = (TextView) itemView.findViewById(R.id.tv_cate);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
            int temp = MyApplication.getDefaultLanguage();
            if (temp == 1) {
                tv_cate.setText(resultp.get("c_ar_title"));
            } else {
                tv_cate.setText(resultp.get("c_title"));
            }

            tv_cate.setTypeface(custom_font);
            return itemView;
        }

    }





















    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        String latitide,longitud,title,desc;
        final ProgressDialog pDialog = new ProgressDialog(SellerAdvDetailsActivity.this);
        public UploadFileToServer(String latitide, String longitud, String title, String desc) {
            this.latitide = latitide;
            this.longitud = longitud;
            this.title = title;
            this.desc = desc;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
          //  progressBar.setProgress(0);

            pDialog.setMessage("Loading...");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String radius = saveSharedPrefrence.getpackageRadius(SellerAdvDetailsActivity.this);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Urlcollection.url);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                              //  publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                Log.e("title", title + " " + desc);


                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("uploaded_file", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("action",
                        new StringBody("addAdvertise"));
                entity.addPart("u_id", new StringBody(u_id));
                entity.addPart("c_id", new StringBody(c_id));
                entity.addPart("latitude", new StringBody(latitide));
                entity.addPart("longitude", new StringBody(longitud));
                entity.addPart("title", new StringBody(title, Charset.forName(HTTP.UTF_8)));
                entity.addPart("description", new StringBody(desc, Charset.forName(HTTP.UTF_8)));
                entity.addPart("pkg_id", new StringBody(pkg_id, Charset.forName(HTTP.UTF_8)));
                entity.addPart("radius", new StringBody(radius, Charset.forName(HTTP.UTF_8)));

             //   totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("entity", "Response from server: " + result);
            pDialog.dismiss();
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your advertise has been added successfully.").setTitle("Success!!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        Intent intent = new Intent(SellerAdvDetailsActivity.this, SellerAddAdvertise.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }






}
