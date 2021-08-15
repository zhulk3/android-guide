package com.longkai.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartUpReceiver extends BroadcastReceiver {
  private static final String TAG = "StartUpReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "onReceive: " + intent.getAction());
    boolean isAlarmOn = QueryPreferences.getIsAlarmOn(context);
    PollService.setServiceAlarm(context, isAlarmOn); //设备重启后计时器失效
  }
}
