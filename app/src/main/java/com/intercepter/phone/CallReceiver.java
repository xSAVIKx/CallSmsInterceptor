package com.intercepter.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

    static MyPhoneStateListener listener;

    public CallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (listener == null)
            listener = new MyPhoneStateListener(context);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(Intent.EXTRA_PHONE_NUMBER)) {
                String number = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
                if (!number.isEmpty()) {
                    listener.setSavedNumber(number);
                }
            }

        }
        telephony.listen(listener, MyPhoneStateListener.LISTEN_CALL_STATE);
    }
}
