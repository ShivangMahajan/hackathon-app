package com.example.shivam.project1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private static int countPowerOff = 0;

    public MyReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {


        final long[] pattern = {0, 1000};
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            countPowerOff++;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            countPowerOff++;
        }

        if (countPowerOff >= 5) {
            countPowerOff = 0;
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage("6239027206",null, "Hello", null, null);
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern1 = new long[]{0, 2000};
            vibrator.vibrate(pattern1, -1);
            Toast.makeText(context, "Message Sent ", Toast.LENGTH_LONG).show();
        }




    }
}
