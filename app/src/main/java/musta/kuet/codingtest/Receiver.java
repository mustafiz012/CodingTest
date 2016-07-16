package musta.kuet.codingtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by musta on 7/11/2016.
 */
public class Receiver extends BroadcastReceiver {
    String name = "";
    String number = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        if (bundle != null){
            Object[] pdus = (Object[])bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i=0; i<messages.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                name += messages[i].getMessageBody().toString().trim();
                name += "\n";
                number = messages[i].getOriginatingAddress();
                Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
                Cursor cursor = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null,null);
                try {
                    cursor.moveToFirst();
                    String displayName = cursor.getString(0);
                    String ContactName = displayName;
                    Toast.makeText(context, "Calling from "+ContactName, Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }finally {
                    cursor.close();
                }
//                int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//                name = cursor.getString(nameIndex);
//                cursor.close();
            }
        }
        //Toast.makeText(context, name, Toast.LENGTH_LONG).show();
        Log.i("Receiver", "Airplane Mode");
    }
}

//