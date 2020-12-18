package com.example.database.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.database.data.UserModel;

public class LocalStorage {
    private static SharedPreferences mSharedPref;
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    public static final String IS_SELECT = "IS_SELECT";

    private LocalStorage() {

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

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static boolean isLogin() {
        return mSharedPref.getBoolean("IS_LOGIN", false);
    }

    public static void setLogin(boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean("IS_LOGIN", value);
        prefsEditor.apply();
    }

    public static void setUser(UserModel userModel) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putLong("id", userModel.getId());
        prefsEditor.putString("name", userModel.getName());
        prefsEditor.putString("login", userModel.getLogin());
        prefsEditor.putString("password", userModel.getPassword());
        prefsEditor.putString("image", userModel.getImage());
        prefsEditor.apply();
    }

    public static UserModel getUser() {
        UserModel userModel = new UserModel();
        userModel.setId(mSharedPref.getLong("id", 0));
        userModel.setName(mSharedPref.getString("name", ""));
        userModel.setLogin(mSharedPref.getString("login", ""));
        userModel.setPassword(mSharedPref.getString("password", ""));
        userModel.setImage(mSharedPref.getString("image", ""));
        return userModel;

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
}
