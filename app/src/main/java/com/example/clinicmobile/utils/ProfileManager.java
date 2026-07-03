package com.example.clinicmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {

    private static final String PREF_NAME = "clinic_profile_prefs";
    private static final String KEY_PROFILE_COMPLETED = "profile_completed";
    private static final String KEY_PROMPT_SHOWN = "profile_prompt_shown";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isProfileCompleted(Context context) {
        return getPrefs(context).getBoolean(KEY_PROFILE_COMPLETED, false);
    }

    public static void setProfileCompleted(Context context, boolean completed) {
        getPrefs(context).edit().putBoolean(KEY_PROFILE_COMPLETED, completed).apply();
    }

    public static boolean isPromptShown(Context context) {
        return getPrefs(context).getBoolean(KEY_PROMPT_SHOWN, false);
    }

    public static void setPromptShown(Context context) {
        getPrefs(context).edit().putBoolean(KEY_PROMPT_SHOWN, true).apply();
    }
}
