package com.kodeinc.fmclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null){

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            String url = "";

            if(remoteMessage.getData() != null){
                url = remoteMessage.getData().get("image");
                //image url will be sent with data payload
                //key image

                if(!TextUtils.isEmpty(url)){
                    final String finalurl = url;

                    //create thread to load image
                   new Handler(Looper.myLooper())
                           .post(new Runnable() {
                               @Override
                               public void run() {
                                   Picasso.get()
                                           .load(finalurl)
                                           .into(new Target() {
                                               @Override
                                               public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                    showNotification(MyFirebaseMessagingService.this,
                                                            title,body,null,bitmap );
                                               }

                                               @Override
                                               public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                               }

                                               @Override
                                               public void onPrepareLoad(Drawable placeHolderDrawable) {

                                               }
                                           });
                               }
                           });
                }
            }


            Toast.makeText(this.getApplicationContext(), "Dumba", Toast.LENGTH_LONG).show();

            //  showNotification(remoteMessage.getData().get("message"));
            showNotification(remoteMessage.getMessageId());


        }



    }


    private  void showNotification(Context context,
                             String title,String body,
                             Intent pendingIntent,
                             Bitmap bitmap){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = new Random().nextInt();

        String channelId = "admtdev-911";
        String channelName = "EDMTDev";

    }
    @Override
    public void onNewToken(@NonNull String s) {

        super.onNewToken(s);
        Log.d("TOKEN UPDATE",s);

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


        Bitmap myBitmap = getBitmapfromUrl("https://image.shutterstock.com/z/stock-photo-white-transparent-leaf-on-mirror-surface-with-reflection-on-turquoise-background-macro-artistic-1029171697.jpg");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification noti = new Notification.Builder(this)
                .setContentTitle(Message)
                .setContentText(Message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .build();

        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this,"cdsd")
                .setAutoCancel(false)
                .setContentTitle(Message)
                .setContentText("Interesting Turn of Events")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(myBitmap))
                .setContentIntent(pendingIntent);



//        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.new_post)
//                .setContentTitle(imageTitle)
//                .setContentText(imageDescription)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(myBitmap))
//                .build();
//


        NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }



    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }



}
