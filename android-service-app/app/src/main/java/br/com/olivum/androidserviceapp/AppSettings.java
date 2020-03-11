package br.com.olivum.androidserviceapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
    private static String DEFAULT_SERVER_ADDRESS = "";

    public static String getString(int keyId) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getResources().getString(R.string.SETTINGS_FILE_NAME),
                Context.MODE_PRIVATE);

        String value = sharedPreferences.getString(App.getContext().getResources().getString(keyId), "");

        return value;
    }

    public static void setString(int keyId, String value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getResources().getString(R.string.SETTINGS_FILE_NAME),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(App.getContext().getResources().getString(keyId), value);

        editor.apply();
    }

    public static Boolean getBoolean(int keyId, Boolean defaultValue) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getResources().getString(R.string.SETTINGS_FILE_NAME),
                Context.MODE_PRIVATE);

        Boolean value = sharedPreferences.getBoolean(App.getContext().getResources().getString(keyId), defaultValue);

        return value;
    }

    public static void setBoolean(int keyId, Boolean value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getResources().getString(R.string.SETTINGS_FILE_NAME),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(App.getContext().getResources().getString(keyId), value);

        editor.apply();
    }
}
