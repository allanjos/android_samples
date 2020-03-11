package br.com.olivum.androidserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {
    private static final String TAG = "TestService";
    private final IBinder binder = new TestServiceBinder();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        return START_STICKY;
    }

    public class TestServiceBinder extends Binder {
        TestService getService() {
            Log.d(TAG, "Binder.getService()");

            return TestService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind()");

        return binder;
    }
}
