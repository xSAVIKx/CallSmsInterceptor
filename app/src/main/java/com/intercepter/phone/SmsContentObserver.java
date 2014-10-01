package com.intercepter.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import java.util.ArrayList;

public class SmsContentObserver extends ContentObserver {

    private static final String CONTENT_SMS = "content://sms/";
    private static final String SMS_ID = "_id";
    private static final String SMS_TYPE = "type";
    private static final String SMS_ADDRESS = "address";
    private static final String SMS_MESSAGE_TYPE_FAILED = "5";
    private static final String SMS_MESSAGE_TYPE_QUEUED = "6";

    Context context;
    private ArrayList<String> blockedNumbers;
    private Uri smsUri;

    public SmsContentObserver(Context context, ArrayList<String> blockedNumbers) {
        super(null);
        this.context = context;
        this.blockedNumbers = blockedNumbers;
        smsUri = Uri.parse(CONTENT_SMS);
    }


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Cursor cursor = context.getContentResolver().query(smsUri, null, null, null, null);
        if (cursor.moveToNext()) {
            String messageId = cursor.getString(cursor.getColumnIndex(SMS_ID));
            String messageType = cursor.getString(cursor.getColumnIndex(SMS_TYPE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(SMS_ADDRESS));
            if (blockedNumbers.contains(phoneNumber)) {
                if (messageType.equals(String.valueOf(SMS_MESSAGE_TYPE_QUEUED))) {
                    ContentValues values = new ContentValues();
                    values.put(SMS_TYPE, SMS_MESSAGE_TYPE_FAILED);
                    String where = Telephony.Sms._ID + "=" + messageId;
                    context.getContentResolver().update(smsUri, values, where, null);
                } else if (messageType.equals(String.valueOf(SMS_MESSAGE_TYPE_FAILED))) {
                    String where = SMS_ID + "=?";
                    context.getContentResolver().delete(smsUri, where, new String[]{SMS_ID});
                }
            }
        }
    }
}
