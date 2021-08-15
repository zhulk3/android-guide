package com.longkai.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

  public static Intent newIntent(Context context) {
    return new Intent(context, MainActivity.class);
  }

  @Override
  protected Fragment createFragment() {
    return PhotoGalleryFragment.newInstance();
  }
}