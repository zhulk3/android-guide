package com.longkai.photogallery;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PollService extends IntentService {
  private static final String TAG = "PollService";
  private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
  public static final String ACTION_SHOW_NOTIFICATION =
      "com.longkai.photogallery.SHOW_NOTIFICATION";
  public static final String PERM_PRIVATE = "com.longkai.photogallery.PRIVATE";
  public static final String REQUEST_CODE = "REQUEST_CODE";
  public static final String NOTIFICATION = "NOTIFICATION";

  public PollService() {super("");}

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public PollService(String name) {
    super(name);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (!isNetworkAvailableAndConnected()) {
      return;
    }
    Log.d(TAG, "onHandleIntent: " + intent);

    String query = QueryPreferences.getStoredQuery(this);
    String lastResultId = QueryPreferences.getPrefLastResultId(this);
    List<GalleryItem> galleryItems;
    if (query == null) {
      galleryItems = new FlickrFetcher().fetchRecentPhotos();
    } else {
      galleryItems = new FlickrFetcher().searchPhotos(query);
    }

    if (galleryItems == null || galleryItems.size() <= 0) {
      return;
    }
    String resultId = galleryItems.get(0).getId();
    if (resultId.equals(lastResultId)) {
    } else {
      Resources resources = getResources();
      Intent i = MainActivity.newIntent(this);
      PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
      Notification notification = new NotificationCompat.Builder(this)
          .setTicker(resources.getString(R.string.new_pictures_title))
          .setSmallIcon(android.R.drawable.ic_menu_report_image)
          .setContentTitle(resources.getString(R.string.new_pictures_title))
          .setContentText(resources.getString(R.string.new_pictures_text))
          .setContentIntent(pendingIntent).setAutoCancel(true)
          .build();
//      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//      notificationManager.notify(0, notification);
//      sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE);
      showBackgroundNotification(0, notification);
    }
    QueryPreferences.setPrefLastResultId(this, resultId);

  }

  public static Intent newIntent(Context context) {
    return new Intent(context, PollService.class);
  }

  private boolean isNetworkAvailableAndConnected() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
    boolean isNetworkConnected =
        isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();
    return isNetworkConnected;
  }

  public static void setServiceAlarm(Context context, boolean isOn) {
    Intent intent = PollService.newIntent(context);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (isOn) {
      alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
          POLL_INTERVAL_MS, pendingIntent);
    } else {
      alarmManager.cancel(pendingIntent);
      pendingIntent.cancel();
    }
    QueryPreferences.setIsAlarmOn(context, isOn);
  }

  public static boolean isServiceAlarmOn(Context context) {
    Intent intent = newIntent(context);
    PendingIntent pendingIntent =
        PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
    return pendingIntent != null;
  }

  private void showBackgroundNotification(int requestCode, Notification notification) {
    Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
    intent.putExtra(REQUEST_CODE, requestCode);
    intent.putExtra(NOTIFICATION, notification);
    sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
  }
}
