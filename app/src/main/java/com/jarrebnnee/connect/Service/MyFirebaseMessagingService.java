package com.jarrebnnee.connect.Service;

/**
 * Created by Owner on 11/17/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jarrebnnee.connect.DisplayIncomingAdvertise;
import com.jarrebnnee.connect.HomeActivity;
import com.jarrebnnee.connect.JobDetailNotificationActivity;
import com.jarrebnnee.connect.SellerDetailNotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Context appContext;

    private NotificationUtils notificationUtils;
    GetSet getSet;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        appContext=getBaseContext();
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            showToast();
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
         //   Toast.makeText(this, "You received a new notification", Toast.LENGTH_LONG).show();
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");

            JSONObject payload = data.getJSONObject("payload");
            String type_id = payload.getString("cn_noti_type_id");//0:-Post Job,1:-Apply Job,2:-Award Job,3 - Accept job by seller 5 - Rejected job by user 4 - Complete job by user, 6=ads
            String cn_noti_type = payload.getString("cn_noti_type");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);
            Log.e(TAG,"type_id: "+type_id);
            Log.e(TAG,"cn_noti_type: "+cn_noti_type);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                showToast();
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);



                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent;
                if (cn_noti_type.equals("6")) {
                    //  Intent resultIntent= new Intent(getApplicationContext(), DisplayIncomingAdvertise.class);
                    resultIntent= new Intent(getApplicationContext(), DisplayIncomingAdvertise.class);
                    resultIntent.putExtra("type_id", type_id);
                    resultIntent.setAction(Long.toString(System.currentTimeMillis()));
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (imageUrl == "" || imageUrl == null) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                    } else {
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }



                }else{
                    resultIntent = new Intent(getApplicationContext(), JobDetailNotificationActivity.class);
                    resultIntent.putExtra("message", message);
                    getSet = GetSet.getInstance();

                    getSet.setjs_id(type_id);
                    getSet.setc_id(type_id);


                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }
                }

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {

        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        Log.e(TAG, "notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl");
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    void showToast() {
        if (null != appContext) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appContext, "New notification received", Toast.LENGTH_SHORT).show();
                }
            });

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Output yes if can vibrate, no otherwise
            if (v.hasVibrator()) {
                Log.e("Can Vibrate", "YES");
                v.vibrate(500);
            } else {
                Log.e("Can Vibrate", "NO");
            }

        }
    }
}