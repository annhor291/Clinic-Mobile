package com.example.clinicmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "clinic_token_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROLE = "user_role";

    private static final String KEY_PROFILE_ID = "profile_id";


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Token
    public static void saveTokens(Context context, String accessToken, String refreshToken) {
        getPrefs(context).edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public static String getAccessToken(Context context) {
        return getPrefs(context).getString(KEY_ACCESS_TOKEN, null);
    }

    public static String getRefreshToken(Context context) {
        return getPrefs(context).getString(KEY_REFRESH_TOKEN, null);
    }

    public static boolean hasToken(Context context) {
        return getAccessToken(context) != null;
    }

    // User info
    public static void saveUserInfo(Context context, Long userId, String email,
                                    String name, String role, Long profileId) {
        getPrefs(context).edit()
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_ROLE, role)
                .putLong(KEY_PROFILE_ID, profileId != null ? profileId : -1)
                .apply();
    }

    public static Long getProfileId(Context context) {
        return getPrefs(context).getLong(KEY_PROFILE_ID, -1);
    }

    public static Long getUserId(Context context) {
        return getPrefs(context).getLong(KEY_USER_ID, -1);
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(KEY_USER_EMAIL, "");
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(KEY_USER_NAME, "");
    }

    public static String getUserRole(Context context) {
        return getPrefs(context).getString(KEY_USER_ROLE, "");
    }

    // Xóa toàn bộ khi logout
    public static void clearAll(Context context) {
        getPrefs(context).edit().clear().apply();
    }
}
