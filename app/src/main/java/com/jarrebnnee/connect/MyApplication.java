package com.jarrebnnee.connect;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

/**
 * Created by Vardhman Infonet 4 on 17-Mar-17.
 */
@ReportsCrashes(formKey = "", mailTo = "harshadkundaliya7959@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = (R.string.acra_toast))
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    static SaveSharedPrefrence preference;
    public String lang = "";

    private static MyApplication mInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        ACRA.init(this);
        super.onCreate();
        mInstance = this;
        preference= new SaveSharedPrefrence();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static int getDefaultLanguage() {
       String loc= Locale.getDefault().getISO3Language();
        String myLang = preference.getlang(mInstance);
        Log.e("9999", Urlcollection.lang+"\t"+myLang);
        if (loc.equals("ara")||Urlcollection.lang.equals("arabic")||myLang.equals("arabic")) {
         /*   if (Urlcollection.lang.equals("english") || myLang.equals("english")) {
                return 2;
            } else {*/
            if (Urlcollection.lang.equals("english")) {
                return 2;
            } else {
                return 1;
            }

           // }
        } else {
          /*  if (Urlcollection.lang.equals("arabic") || myLang.equals("arabic")) {
                return 1;
            } else {*/
                return 2;
           // }
        }
    }
}
