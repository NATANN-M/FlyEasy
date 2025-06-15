package com.example.flyeasy.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

public class SessionManager {
    private static final String PREF_NAME = "FlyEasySession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";

    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public void saveProfileImageUri(String uri) {
        prefs.edit().putString(KEY_PROFILE_IMAGE_URI, uri).apply();
    }

    public String getProfileImageUri() {
        return prefs.getString(KEY_PROFILE_IMAGE_URI, null);
    }
public void saveUserName(String Name){

        editor.putString("Name",Name);
        editor.apply();
}
    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1); // -1 means not logged in
    }

    public void saveUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "user"); // default to "user"
    }
public String getUserName(){

        return prefs.getString("Name","No UserName");
}
    public void logout() {


        editor.clear();
        editor.apply();

    }

    public void clearSessionData() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }
}
