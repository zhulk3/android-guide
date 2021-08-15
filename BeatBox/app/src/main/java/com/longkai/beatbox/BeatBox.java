package com.longkai.beatbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

public class BeatBox {
  private static final String TAG = "BeatBox";
  private static final String SOUND_FOLDER = "sample_sounds";
  private static final int MAX_SOUNDS = 5;
  private AssetManager mAssetManager;
  private List<Sound> mSounds = new ArrayList();
  private SoundPool mSoundPool;

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public BeatBox(Context context) {
    mAssetManager = context.getAssets();
    mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    loadSounds();
  }

  public List<Sound> getSounds() {
    return mSounds;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void loadSounds() {
    String[] soundNames;
    try {
      soundNames = mAssetManager.list(SOUND_FOLDER);
      Log.i(TAG, "Found " + soundNames.length + "sounds");
    } catch (IOException e) {
      Log.e(TAG, "could not list assets", e);
      return;
    }
    for (String soundName : soundNames) {
      String assetsPath = SOUND_FOLDER + "/" + soundName;
      Sound sound = new Sound(assetsPath);
      load(sound);
      mSounds.add(sound);
    }
  }

  public void play(Sound sound){
    Integer integer = sound.getSoundId();
    if (integer!=null){
      mSoundPool.play(integer,1.0f,1.0f,1,0,1.0f);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void load(Sound sound) {
    try (AssetFileDescriptor assetFileDescriptor = mAssetManager.openFd(sound.getAssetPath())) {
      int soundId = mSoundPool.load(assetFileDescriptor, 1);
      sound.setSoundId(soundId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void release(){
    mSoundPool.release();
  }


}
