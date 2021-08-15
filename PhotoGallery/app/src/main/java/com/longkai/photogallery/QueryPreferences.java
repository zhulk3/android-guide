package com.longkai.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
  private static final String PREF_SEARCH_QUERY = "query";
  private static final String PREF_LAST_RESULT_ID = "lastResultId";
  private static final String IS_ALARM_ON = "isAlarmOn";

  public static String getStoredQuery(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context)
        .getString(PREF_SEARCH_QUERY, null);
  }

  public static void setStoredQuery(Context context, String text) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_SEARCH_QUERY, text)
        .apply();
  }

  public static String getPrefLastResultId(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context)
        .getString(PREF_LAST_RESULT_ID, null);
  }

  public static void setPrefLastResultId(Context context, String lastResultId) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
        .putString(PREF_LAST_RESULT_ID, lastResultId).apply();
  }

  public static void setIsAlarmOn(Context context, boolean isAlarmOn) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(IS_ALARM_ON, isAlarmOn)
        .apply();
  }

  public static boolean getIsAlarmOn(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_ALARM_ON, false);
  }
}
