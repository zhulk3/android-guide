package com.longkai.photogallery;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

public class ThumbnailDownload<T> extends HandlerThread {
  private static final String TAG = "ThumbnailDownload";
  private boolean mHasQuit = false;
  private static final int MESSAGE_DOWNLOAD = 0;
  private Handler mRequestHandler;
  private ConcurrentHashMap<T, String> mRequestMap = new ConcurrentHashMap<>();
  private Handler mResponseHandle;
  private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

  public interface ThumbnailDownloadListener<T> {
    void onThumbnailDownloaded(T target, Bitmap thumbnail);
  }

  public ThumbnailDownload(String name, Handler responseHandle) {
    super(name);
    mResponseHandle = responseHandle;
  }

  public void setThumbnailDownloadListener(
      ThumbnailDownloadListener<T> ThumbnailDownloadListener) {
    mThumbnailDownloadListener = ThumbnailDownloadListener;
  }

  @Override
  protected void onLooperPrepared() {
    mRequestHandler = new Handler() {
      @Override
      public void handleMessage(@NonNull Message msg) {
        if (msg.what == MESSAGE_DOWNLOAD) {
          T target = (T) msg.obj;
          handleRequest(target);
        }
      }
    };
  }

  private void handleRequest(final T target) {
    try {
      final String url = mRequestMap.get(target);
      if (url == null) {
        return;
      }
      byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(url);
      final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
      mResponseHandle.post(new Runnable() {
        @Override
        public void run() {
          if (mRequestMap.get(target)!=url||mHasQuit){
            return;
          }
          mRequestMap.remove(target);
          mThumbnailDownloadListener.onThumbnailDownloaded(target,bitmap);//运行在主线程当中
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean quit() {
    mHasQuit = true;
    return super.quit();
  }

  public void queueThumbnail(T target, String url) {
    Log.i(TAG, "Got a url: " + url);
    if (url == null) {
      mRequestMap.remove(target);
    } else {
      mRequestMap.put(target, url);
      mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
    }
  }
  public void clearQueue(){
    mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    mRequestMap.clear();
  }
}
