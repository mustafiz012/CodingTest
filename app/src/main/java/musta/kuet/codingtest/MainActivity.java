package musta.kuet.codingtest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ResultActivity resultActivity = new ResultActivity();
    boolean isOnStop = false, isOnPause = false, isOnDestroy = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //resultActivity.builder.setAutoCancel(true);

    }

    //a custom broadcast receiver
    public void customBroadcastReceiver(View view){
        Intent intent = new Intent();
        intent.setAction("musta.kuet.codingtest.CUSTOM_INTENT_TEST");
        sendBroadcast(intent);
    }

//    //creating notification alert
    public void bigNotificationView(){
        NotificationCompat.Builder bigBuilder = new NotificationCompat.Builder(this);
        bigBuilder.setSmallIcon(R.mipmap.ic_launcher);
        bigBuilder.setContentTitle("Big Notification");
        bigBuilder.setContentText("Hi, this is big notification!");
        bigBuilder.setTicker("New Big Notification Alert!");

        //adding big view specific configuration
        NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is 1st line....");
        events[1] = new String("This is 2nd line...");
        events[2] = new String("This is 3rd line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        //set a title for the inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        //moves events into the big view
        for (int i=0; i<events.length; i++){
            inboxStyle.addLine(events[i]);
        }

        bigBuilder.setStyle(inboxStyle);
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(ResultActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        bigBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9999, bigBuilder.build());
        bigBuilder.setAutoCancel(true);
    }

    //custom broadcast intent
    public void broadcastIntent(View view){
        Intent intent = new Intent();
        intent.setAction("musta.kuet.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        isOnDestroy = true;
        if (!isOnStop && !isOnPause)
        {
            bigNotificationView();
            //resultActivity.notificationView();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        if (!isOnStop && !isOnDestroy)
        {
            bigNotificationView();
            //resultActivity.notificationView();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        isOnStop = true;
        if (!isOnDestroy && !isOnPause)
        {
            bigNotificationView();
            //resultActivity.notificationView();
        }
        super.onStop();
    }
}
