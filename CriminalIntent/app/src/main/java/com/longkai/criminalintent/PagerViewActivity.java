package com.longkai.criminalintent;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PagerViewActivity extends AppCompatActivity {
  private ViewPager mViewPager;
  private List<Crime> mCrimes;
  private static final String EXTRA_CRIME_ID = "com.longkai.criminalintent.CRIME_ID";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pager_view);
    mViewPager = findViewById(R.id.crime_view_pager);
    mCrimes = CrimeLab.get(this).getCrimes();
    UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID );

    FragmentManager fragmentManager = getSupportFragmentManager();
    mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager,1) {
      @NonNull
      @Override
      public Fragment getItem(int position) {
        Crime crime = mCrimes.get(position);
        return CrimeFragment.newInstance(crime.getId());
      }

      @Override
      public int getCount() {
        return mCrimes.size();
      }
    });

    for (int i = 0;i < mCrimes.size();i++){
      if (mCrimes.get(i).getId().equals(uuid)){
        mViewPager.setCurrentItem(i);
        break;
      }
    }
  }

  public static Intent newIntent(Context context, UUID uuid) {
    Intent intent = new Intent(context, PagerViewActivity.class);
    intent.putExtra(EXTRA_CRIME_ID, uuid);

    return intent;
  }
}