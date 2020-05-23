package com.kodeinc.fmclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public FirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("message"));
    }

    @Override
    public void onNewToken(@NonNull String s) {

        try{
          String mToken =   FirebaseInstanceId.getInstance().getToken(s,s);
            Log.e("TOKEN",mToken);
            Toast.makeText(this, mToken, Toast.LENGTH_LONG).show();

        }catch (IOException e){

        }



    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    public void showNotification(String Message){
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification noti = new Notification.Builder(this)
                .setContentTitle("Notificaton")
                .setContentText(Message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .build();

        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this,null)
                .setAutoCancel(true)
                .setContentTitle("Testing Me MOver")
                .setContentText(Message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);


        NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }
}
