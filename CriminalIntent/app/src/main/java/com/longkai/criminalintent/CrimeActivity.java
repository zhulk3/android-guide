package com.longkai.criminalintent;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CrimeActivity extends SingleFragmentActivity {

  private static final String EXTRA_CRIME_ID = "com.longkai.criminalintent";

  @Override
  protected Fragment createFragment() {
    UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    return CrimeFragment.newInstance(uuid);
  }

  public static Intent newIntent(Context context, UUID uuid) {
    Intent intent = new Intent(context, CrimeActivity.class);
    intent.putExtra(EXTRA_CRIME_ID,uuid);
    return intent;
  }
}