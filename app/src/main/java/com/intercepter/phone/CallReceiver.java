package com.intercepter.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {

    public CallReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            processOutgoingCall(context, intent);
        }
    }

    private void processOutgoingCall(Context context, Intent intent) {
        String phoneNumber = getPhoneNumber(intent);
        if (isBlocked(phoneNumber)) {
            endCall();
            showToast(context, phoneNumber);
        }
    }

    private String getPhoneNumber(Intent intent) {
        String phoneNumber = getResultData();
        if (phoneNumber == null || phoneNumber.isEmpty())
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        return phoneNumber;
    }

    private boolean isBlocked(String phoneNumber) {
        return MainActivity.enabled && MainActivity.isInNumbers(phoneNumber);
    }

    private void endCall() {
        setResultData(null);
    }

    private void showToast(Context context, String phoneNumber) {
        Toast.makeText(context, "Call to " + phoneNumber + " was blocked", Toast.LENGTH_LONG).show();
    }


}

