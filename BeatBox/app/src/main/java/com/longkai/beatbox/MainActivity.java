package com.longkai.beatbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

  @Override
  protected Fragment createFragment() {
    return BeatBoxFragment.newInstance();
  }


}