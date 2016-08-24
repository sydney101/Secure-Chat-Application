package syd.s_chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {
    public void onReceive (Context context, Intent intent){
        //Get messages passed in.
        //Initializing some values.
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        //Message initialized as null gets assigned to the information sent in as text.

        String str="";
        if(bundle!=null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");

            messages=new SmsMessage[pdus.length];
            for(int i=0; i<messages.length; i++)
            {
                //Loop through our messages.
                messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);

                str+="Message From" + messages[i].getOriginatingAddress();
                str+=":";
                str+=messages[i].getMessageBody().toString();
                str+="/n";

            }
            //Display the message
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            //Send a broadcast intent to update the sms received in a textview.
            Intent broadcastIntent=new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);

        }
    }
}
