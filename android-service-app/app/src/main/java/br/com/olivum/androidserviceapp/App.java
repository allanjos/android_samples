package br.com.olivum.androidserviceapp;

import android.app.Application;
import android.content.Context;

public class App extends Application
{
    private static final String TAG = "App";
    private static App instance;

    public static App getAppInstance() {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}