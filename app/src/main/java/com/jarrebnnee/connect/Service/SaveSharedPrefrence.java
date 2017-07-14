package com.jarrebnnee.connect.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveSharedPrefrence {

    Context context;
    boolean isSuccess;
    SharedPreferences sharedPreferences;

    public static final String PREFS_NAME = "MyPref";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_USER_FNAME = "key_user_fname";
    public static final String KEY_USER_LNAME = "key_user_lname";
    public static final String KEY_USER_EMAIL = "key_user_email";
    public static final String KEY_USER_PHONE = "key_user_phone";
    public static final String KEY_USER_IMAGE = "key_user_image";
    public static final String KEY_USER_COUNTRY = "key_user_country";
    public static final String KEY_USER_CITY = "key_user_city";
    public static final String KEY_USER_GENDER = "key_user_gender";
    public static final String KEY_SOCIAL_ACTION = "key_social_action";
    public static final String KEY_DEVICE_TOKEN = "key_device_token";
    public static final String KEY_USER_TYPE = "key_user_type";
    public static final String SM_SOCIAL_PROVIDER_ID = "sm_social_provider_id";
    public static final String KEY_USER_LATITUTE = "key_user_latitute";
    public static final String KEY_USER_LONGITUTE = "key_user_longitute";
    public static final String KEY_pkg_id = "key_pkg_id";
    public static final String KEY_packageName = "key_packageName";
    public static final String KEY_packageRadius = "key_packageRadius";
    public static final String KEY_no_of_advertise = "key_no_of_advertise";
    public static final String KEY_paymentAmount = "key_paymentAmount";
    public static final String KEY_notificationSeconds = "key_notificationSeconds";
    public static final String u_is_notification_sound = "u_is_notification_sound";
    public static final String LANG = "lang";
    public static String Seller_Has_Service;
    public static String isNewRegistered;

    public void saveUserLongitute(Context context, String longitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_LONGITUTE, longitute);


        editor.commit();

    }

    public String getUserLongitute(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_USER_LONGITUTE, "");

        return longitute;
    }

    public void saveUserLatitute(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_LATITUTE, latitute);


        editor.commit();

    }

    public boolean getSellerHasService(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        boolean longitute = sharedPreferences.getBoolean(Seller_Has_Service, false);

        return longitute;
    }

    public void saveSellerHasService(Context context, boolean b) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putBoolean(Seller_Has_Service, b);


        editor.commit();

    }


    public String getUserLatitute(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String latitute = sharedPreferences.getString(KEY_USER_LATITUTE, "");

        return latitute;
    }


    public void saveNewReg(Context context, boolean lang) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putBoolean(isNewRegistered, lang);


        editor.commit();

    }

    public boolean getNewReg(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        boolean lang = sharedPreferences.getBoolean(isNewRegistered, false);

        return lang;
    }


    public void savelang(Context context, String lang) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(LANG, lang);


        editor.commit();

    }

    public String getlang(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString(LANG, "");

        return lang;
    }

    public void saveNotificationSeconds(Context context, String lang) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_notificationSeconds, lang);


        editor.commit();

    }

    public String getNotificationSeconds(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString(KEY_notificationSeconds, "" + 10);

        return lang;
    }

    public void saveu_is_notification_sound(Context context, String u_is_notification_sound) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(SM_SOCIAL_PROVIDER_ID, u_is_notification_sound);


        editor.commit();

    }

    public String getu_is_notification_sound(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String u_is_notification_sound = sharedPreferences.getString(SM_SOCIAL_PROVIDER_ID, "");

        return u_is_notification_sound;
    }

    public void saveSM_SOCIAL_PROVIDER_ID(Context context, String sm_social_provider_id) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(SM_SOCIAL_PROVIDER_ID, sm_social_provider_id);


        editor.commit();

    }

    public String getSM_SOCIAL_PROVIDER_ID(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String sm_social_provider_id = sharedPreferences.getString(SM_SOCIAL_PROVIDER_ID, "");

        return sm_social_provider_id;
    }

    public void saveUserType(Context context, String key_user_type) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_TYPE, key_user_type);


        editor.commit();

    }

    public String getUserType(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String key_user_type = sharedPreferences.getString(KEY_USER_TYPE, "");

        return key_user_type;
    }

    public void saveDeviceToken(Context context, String key_device_token) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DEVICE_TOKEN, key_device_token);


        editor.commit();

    }

    public String getDeviceToken(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String key_device_token = sharedPreferences.getString(KEY_DEVICE_TOKEN, "");

        return key_device_token;
    }

    public void saveSocialAction(Context context, String key_social_action) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SOCIAL_ACTION, key_social_action);


        editor.commit();

    }

    public String getSocialAction(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String key_social_action = sharedPreferences.getString(KEY_SOCIAL_ACTION, "");

        return key_social_action;
    }

    public void saveUserID(Context context, String user_id) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, user_id);


        editor.commit();

    }

    public String getUserID(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString(KEY_USER_ID, "");

        return user_id;
    }

    public void saveUserImage(Context context, String user_image) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_IMAGE, user_image);


        editor.commit();

    }

    public String getUserImage(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_image = sharedPreferences.getString(KEY_USER_IMAGE, "");

        return user_image;
    }


    public String getpkg_id(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_pkg_id, "");

        return longitute;
    }

    public void savepkg_id(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_pkg_id, latitute);


        editor.commit();

    }


    public String getpaymentAmount(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_paymentAmount, "");

        return longitute;
    }

    public void savepaymentAmount(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_paymentAmount, latitute);


        editor.commit();

    }


    public String getpackageRadius(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_packageRadius, "");

        return longitute;
    }

    public void savepackageRadius(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_packageRadius, latitute);


        editor.commit();

    }


    public String getpackageName(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_packageName, "");

        return longitute;
    }

    public void savepackageName(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_packageName, latitute);


        editor.commit();

    }

    public String getno_of_advertise(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String longitute = sharedPreferences.getString(KEY_no_of_advertise, "");

        return longitute;
    }

    public void saveno_of_advertise(Context context, String latitute) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_no_of_advertise, latitute);


        editor.commit();

    }


    public void saveUserFName(Context context, String user_fname) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_FNAME, user_fname);


        editor.commit();

    }

    public String getUserFName(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_fname = sharedPreferences.getString(KEY_USER_FNAME, "");

        return user_fname;
    }

    public void saveUserLName(Context context, String user_lname) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_LNAME, user_lname);


        editor.commit();

    }

    public String getUserLName(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_lname = sharedPreferences.getString(KEY_USER_LNAME, "");

        return user_lname;
    }

    public void saveUserEmail(Context context, String user_email) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_EMAIL, user_email);


        editor.commit();

    }

    public String getUserEmail(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_email = sharedPreferences.getString(KEY_USER_EMAIL, "");

        return user_email;
    }

    public void saveUserPhone(Context context, String user_phone) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_PHONE, user_phone);


        editor.commit();

    }

    public String getUserPhone(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_phone = sharedPreferences.getString(KEY_USER_PHONE, "");

        return user_phone;
    }

    public void saveUserCountry(Context context, String user_country) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_COUNTRY, user_country);


        editor.commit();

    }

    public String getUserCountry(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_country = sharedPreferences.getString(KEY_USER_COUNTRY, "");

        return user_country;
    }

    public void saveUserCity(Context context, String user_city) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_CITY, user_city);


        editor.commit();

    }

    public String getUserCity(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_city = sharedPreferences.getString(KEY_USER_CITY, "");

        return user_city;
    }

    public void saveUserGender(Context context, String user_gender) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_GENDER, user_gender);


        editor.commit();

    }

    public String getUserGender(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String user_gender = sharedPreferences.getString(KEY_USER_GENDER, "");

        return user_gender;
    }

    public void DeletePrefrence(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

}
