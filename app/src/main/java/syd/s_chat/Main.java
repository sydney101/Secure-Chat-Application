package syd.s_chat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends ActionBarActivity {
    //Called when the activity is first created
    Button sendSMS;
    EditText msgTxt;
    EditText numTxt;
    IntentFilter intentFilter;


    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //Display the message in the TextView

            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("sms"));
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //Intent to filter for SMS messages received
        intentFilter = new IntentFilter();

        intentFilter.addAction("SMS_RECEIVED_ACTION");


        //Get the IDs, take the button and set the onClick listener.
        sendSMS = (Button) findViewById(R.id.sendBtn);
        msgTxt = (EditText) findViewById(R.id.message);
        numTxt = (EditText) findViewById(R.id.numberTxt);
        sendSMS.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String myMsg = msgTxt.getText().toString();
                String theNumber = numTxt.getText().toString();
                sendMsg(theNumber, myMsg);
            }
        });
    }

    //The parameter being passed are the number and message.

    protected void sendMsg(String theNumber, String myMsg) {

        //Implements pending Intents
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        //SMSManager sms = SMSManager.getDefault();
        //SMSManager class is not directly instantiated.
        //We are going to use a static method to get an instance of the SMSManager.
        //SMSManager is for sending message to another device.
        //betDefault method: It is a static method in order to get the default instance of the SMSManager.

        //sms.send textMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent);
        //destinationAddress: The phone number of the recipient.
        //scAddress: Service Center Address.
        //text: Content of the message one is sending.
        //sentIntent: Pending intent when a message is sent.
        //deliveryIntent: Pending intent when a message is delivered. Whether it was sent successfully or delivered successfully.

        //sms.sendTextMessage(theNumber, null, myMessage, null, null);


        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive (Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(Main.this, "SMS sent", Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_LONG).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));


        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive(Context arg0, Intent arg1)
            {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_LONG).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not Delivered", Toast.LENGTH_LONG).show();
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);
    }


    @Override
    public void onResume() {
        //Register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }


    @Override
    public void onPause() {
        //Unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

}
