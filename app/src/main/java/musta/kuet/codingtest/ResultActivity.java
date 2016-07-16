package musta.kuet.codingtest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    boolean isOnStop = false, isOnPause = false, isOnDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void customToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        customToast("onDestroy");
        isOnDestroy = true;
        if (!isOnStop && !isOnPause)
            notificationView();
        super.onDestroy();
    }

    //creating notification alert
    public void notificationView(){
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notification test, click me!");
        builder.setContentText("Hi, this is android notification details!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9999, builder.build());
    }

    @Override
    protected void onPause() {
        customToast("onPause");
        isOnPause = true;
        if (!isOnStop && !isOnDestroy)
            notificationView();
        super.onPause();
    }

    @Override
    protected void onStop() {
        customToast("onStop");
        isOnStop = true;
        if (!isOnDestroy && !isOnPause)
            notificationView();
        super.onStop();
    }

}
