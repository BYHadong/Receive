package org.adong.byh.myreceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSreceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive 호출");
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages.length > 0) {
            String sender = messages[0].getOriginatingAddress();
            Log.d(TAG, "sender : " + sender);

            String contents = messages[0].getMessageBody();
            Log.d(TAG, "contents : " + contents);

            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG,"received date : " + receivedDate);
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] obj = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[obj.length];

        for (int i = 0; i<obj.length; i++){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) obj[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) obj[i]);
            }
        }
        return messages;
    }
}
