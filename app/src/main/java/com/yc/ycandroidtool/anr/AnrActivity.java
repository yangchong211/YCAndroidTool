package com.yc.ycandroidtool.anr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.toollib.crash.CrashToolUtils;
import com.yc.ycandroidtool.App;
import com.yc.ycandroidtool.MainActivity;
import com.yc.ycandroidtool.R;

public class AnrActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private TextView minAnrDurationLabel;
    private Button minAnrDuration;
    private TextView reportModeLabel;
    private Button reportMode;
    private TextView behaviourLabel;
    private Button behaviour;
    private Button threadSleep;
    private Button infiniteLoop;
    private Button deadlock;
    private App application = null;
    private final Object _mutex = new Object();
    private int mode = 0;
    private boolean crash = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
        initFindViewById();

        application = (App) getApplication();

        minAnrDuration.setText(application.mDuration + " seconds");
        minAnrDuration.setOnClickListener(this);
        reportMode.setText("All threads");
        reportMode.setOnClickListener(this);
        behaviour.setText("Crash");
        behaviour.setOnClickListener(this);
        threadSleep.setOnClickListener(this);
        infiniteLoop.setOnClickListener(this);
        deadlock.setOnClickListener(this);
    }

    private void initFindViewById() {
        progressBar = findViewById(R.id.progressBar);
        minAnrDurationLabel = findViewById(R.id.minAnrDurationLabel);
        minAnrDuration = findViewById(R.id.minAnrDuration);
        reportModeLabel = findViewById(R.id.reportModeLabel);
        reportMode = findViewById(R.id.reportMode);
        behaviourLabel = findViewById(R.id.behaviourLabel);
        behaviour = findViewById(R.id.behaviour);
        threadSleep = findViewById(R.id.threadSleep);
        infiniteLoop = findViewById(R.id.infiniteLoop);
        deadlock = findViewById(R.id.deadlock);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == minAnrDuration){
            application.mDuration = application.mDuration % 6 + 2;
            minAnrDuration.setText(application.mDuration + " seconds");
        } else if (v == reportMode){
            mode = (mode + 1) % 3;
            switch (mode) {
                case 0:
                    reportMode.setText("All threads");
                    application.anrWatchDog.setReportAllThreads();
                    break ;
                case 1:
                    reportMode.setText("Main thread only");
                    application.anrWatchDog.setReportMainThreadOnly();
                    break ;
                case 2:
                    reportMode.setText("Filtered");
                    application.anrWatchDog.setReportThreadNamePrefix("APP:");
                    break ;
            }
        } else if (v == behaviour){
            crash = !crash;
            if (crash) {
                behaviour.setText("Crash崩溃");
                application.anrWatchDog.setANRListener(null);
            } else {
                behaviour.setText("Silent静默");
                application.anrWatchDog.setANRListener(application.silentListener);
            }
        } else if (v == threadSleep){
            Sleep();
        } else if (v == infiniteLoop){
            InfiniteLoop();
        } else if (v == deadlock){
            _deadLock();
        }
    }

    private static void Sleep() {
        try {
            Thread.sleep(8 * 1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void InfiniteLoop() {
        int i = 0;
        //noinspection InfiniteLoopStatement
        while (true) {
            i++;
        }
    }

    public class LockerThread extends Thread {

        LockerThread() {
            setName("APP: Locker");
        }

        @Override
        public void run() {
            synchronized (_mutex) {
                //noinspection InfiniteLoopStatement
                while (true)
                    Sleep();
            }
        }
    }

    private void _deadLock() {
        new LockerThread().start();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                synchronized (_mutex) {
                    Log.e("ANR-Failed", "There should be a dead lock before this message");
                }
            }
        }, 1000);
    }

}
