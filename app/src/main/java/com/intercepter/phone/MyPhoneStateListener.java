package com.intercepter.phone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

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
        String number;
        if (callingNumber.isEmpty())
            number = savedNumber;
        else {
            number = callingNumber;
        }
        if (!MainActivity.isInNumbers(number))
            return;
        if (!MainActivity.enabled){
            return;
        }
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
            String GET_I_TELEPHONY = "getITelephony";
            Method m = c.getDeclaredMethod(GET_I_TELEPHONY);
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService = (ITelephony) m.invoke(tm);

            telephonyService.silenceRinger();
            telephonyService.endCall();
            Toast.makeText(context, callingNumber + " call blocked", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
