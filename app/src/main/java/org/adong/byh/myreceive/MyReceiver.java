package org.adong.byh.myreceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSreceive";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
            sendToActivity(context,sender, contents, receivedDate);
        }
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate){
        Intent intent = new Intent(context, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_NEW_TASK|
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("contents", contents);
        intent.putExtra("receivedDate", format.format(receivedDate));

        context.startActivity(intent);
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] obj = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[0];
        if (obj != null) {
            messages = new SmsMessage[obj.length];
        }

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
