package com.longkai.photogallery;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (getResultCode() != Activity.RESULT_OK) {
      return;
    }
    int requestCode = intent.getIntExtra(PollService.REQUEST_CODE, 0);
    Notification notification = intent.getParcelableExtra(PollService.NOTIFICATION);
    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
    notificationManagerCompat.notify(requestCode, notification);
  }
}
