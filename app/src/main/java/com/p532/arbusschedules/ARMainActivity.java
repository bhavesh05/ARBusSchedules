package com.p532.arbusschedules;

import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;

public class ARMainActivity extends ARActivity {

    public static final String TAG = "ARMainActivity";
    private RecyclerView recyclerView;
    private BusDetailAdapter busDetailAdapter;
    private List<BusDetail> busDetailList;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armain);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        busDetailList = new ArrayList<>();
        busDetailAdapter = new BusDetailAdapter(this, busDetailList);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentmain_rellayout);
        relativeLayout.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(busDetailAdapter);

        final Map<String,String> timeMap = new HashMap<>();
        BusDataRetrieval busDataRetrieval = new BusDataRetrieval(this,locationManager,timeMap, busDetailList, busDetailAdapter);

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
        ARImageTrackable arImageTrackable = new ARImageTrackable("Bloomington Transit");
        arImageTrackable.loadFromAsset("mobilebus.jpg");

        ARImageTracker arImageTracker = ARImageTracker.getInstance();
        arImageTracker.initialise();

        arImageTracker.addTrackable(arImageTrackable);

        arImageTrackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect(ARImageTrackable arImageTrackable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.contentmain_rellayout);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void didTrack(ARImageTrackable arImageTrackable) {

            }

            @Override
            public void didLose(ARImageTrackable arImageTrackable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.contentmain_rellayout);
                        relativeLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private int dpToPx(int dp) {
        Resources resources = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }
}
