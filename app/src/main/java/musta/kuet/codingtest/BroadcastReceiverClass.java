package musta.kuet.codingtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by musta on 7/6/2016.
 */
public class BroadcastReceiverClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent Detected!!", Toast.LENGTH_LONG).show();
    }
}
