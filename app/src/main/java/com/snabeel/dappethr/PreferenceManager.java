package com.snabeel.dappethr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyProperties;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PreferenceManager {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    private PreferenceManager() {

    }

    public static void init(Context context) {
        if (preferences == null) {
//            preferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            try {
                MasterKey masterKey = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
                preferences = EncryptedSharedPreferences.create(
                        context,
                        "secret_shared_prefs",
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );

            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public static String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    public static void setStringValue(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static boolean getBoolValue(String key) {
        return preferences.getBoolean(key, false);
    }

    public static void setBoolValue(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Integer getIntValue(String key) {
        return preferences.getInt(key, 0);
    }

    public static void setIntValue(String key, Integer value) {
        editor = preferences.edit();
        editor.putInt(key, value).apply();
    }

    public static void deletePref() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
