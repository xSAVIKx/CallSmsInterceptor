package com.intercepter.phone;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class SmsService extends Service {
    private static final String CONTENT_SMS = "content://sms/";

    public SmsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SmsContentObserver smsContentObserver = new SmsContentObserver(getApplicationContext(), MainActivity.list);
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS), true, smsContentObserver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
