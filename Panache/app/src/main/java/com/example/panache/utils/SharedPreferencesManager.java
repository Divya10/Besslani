/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";
    public static final String USER_PIC = "USER_PIC";
    public static final String AVATAR_PIC = "AVATAR_PIC";
    public static final String FRIEND_CNT = "FRIEND_CNT";
    public static final String FOLLOWING_CNT = "FOLLOWING_CNT";
    public static final String FOLLOWER_CNT = "FOLLOWER_CNT";
    public static final String NOTIFICATION_CNT = "NOTIFICATION_CNT";
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    public static final String IS_SELECT = "IS_SELECT";
    public static final String USERNAME = "USERNAME";
    public static final String RATING = "RATING";
    public static final String LOCATIONS = "LOCATIONS";
    public static final String UPDATEDTIMESTAMP = "UPDATEDTIMESTAMP";
    public static final String ONLINESTATUS = "ONLINESTATUS";
    public static final String CURRANT_EVENT = "CURRANT_EVENT";
    public static final String OFFLINE_DATA_LOADED = "OFFLINE_DATA_LOADED";
    public static final String IS_TUTORIAL_LOADED = "IS_TUTORIAL_LOADED";
    private static SharedPreferences mSharedPref;

    private SharedPreferencesManager() {
    }

    public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static void write(String key, long value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }

    public static void clearAll() {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        String deviceToken = read(DEVICE_TOKEN, "");
        prefsEditor.clear();
        write(DEVICE_TOKEN, deviceToken);
        prefsEditor.apply();
    }

}

