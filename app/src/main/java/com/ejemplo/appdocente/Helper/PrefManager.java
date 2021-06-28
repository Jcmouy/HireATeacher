package com.ejemplo.appdocente.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "HireATeacher";

    // Shared Pref Global
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Shared Pref User
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_TYPE_USER = "type_user";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLatLngUser(String lat, String lng) {
        editor.putString(KEY_LAT, lat);
        editor.putString(KEY_LONG, lng);
        editor.commit();
    }

    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public String getLatUser() {
        return pref.getString(KEY_LAT, null);
    }

    public String getLngUser() {
        return pref.getString(KEY_LONG, null);
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public String getMobileNumberLogin() {
        return pref.getString(KEY_MOBILE, null);
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getTypeUser() {
        return pref.getString(KEY_TYPE_USER, null);
    }

    public void createLogin(String id, String name, String username, String email, String mobile, String type_user) {
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_TYPE_USER, type_user);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
        //return pref.getBoolean(KEY_IS_LOGGED_IN, true);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("id", pref.getString(KEY_ID, null));
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("username", pref.getString(KEY_USERNAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("type_user", pref.getString(KEY_TYPE_USER, null));
        profile.put("lat", pref.getString(KEY_LAT, null));
        profile.put("long", pref.getString(KEY_LONG, null));
        return profile;
    }
}
