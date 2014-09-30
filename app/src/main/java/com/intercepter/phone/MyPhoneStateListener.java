package com.intercepter.phone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * Created by iurii on 30.09.14.
 */
public class MyPhoneStateListener extends PhoneStateListener {

    Context context;
    private String savedNumber;

    public MyPhoneStateListener(Context context) {
        super();
        this.context = context;
    }

    public void setSavedNumber(String number) {
        savedNumber = number;
    }

    @Override
    public void onCallStateChanged(int state, String callingNumber) {
        super.onCallStateChanged(state, callingNumber);
        String number = "";
        if (callingNumber.isEmpty())
            number = savedNumber;
        else {
            number = callingNumber;
        }
        if (!MyListAdapter.isInNumbers(number))
            return;
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //handle out going call
                endCallIfBlocked(number);
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                //handle in coming call
                endCallIfBlocked(number);
                break;

            default:
                break;
        }
    }

    private void endCallIfBlocked(String callingNumber) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService = (ITelephony) m.invoke(tm);

            telephonyService.silenceRinger();
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
