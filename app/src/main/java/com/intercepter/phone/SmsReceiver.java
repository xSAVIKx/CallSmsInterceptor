package com.intercepter.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SmsReceiver extends BroadcastReceiver {
    private static final String CONTENT_SMS = "content://sms/";

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsContentObserver smsContentObserver = new SmsContentObserver(context, MainActivity.list);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS), true, smsContentObserver);
    }
}
