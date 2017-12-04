package com.p532.arbusschedules;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import eu.kudan.kudan.ARActivity;

public class ARMainActivity extends ARActivity {

    public static final String TAG = "ARMainActivity";
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_armain);
        final Map<String,String> timeMap = new HashMap<>();
        BusDataRetrieval busDataRetrieval = new BusDataRetrieval(this,locationManager,timeMap);

        busDataRetrieval.getBusData();
        Log.d(TAG, "onCreate: " + timeMap);

        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + timeMap);
            }
        }, 10000);

    }
    @Override
    public void setup() {
        super.setup();
    }
}
