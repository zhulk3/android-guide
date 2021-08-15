package com.longkai.criminalintent;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrimeListFragment extends Fragment {
  private RecyclerView mRecyclerView;
  private CrimeAdapter mCrimeAdapter;
  private static final int REQUEST_CODE = 1;
  private static final String SUBTITLE_VISIBLE = "subtitle";
  private boolean mSubtitleVisible;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mSubtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE);
    }
    View view = inflater.inflate(R.layout.fragment_list_crime, container, false);
    mRecyclerView = view.findViewById(R.id.crime_list_recyclerview);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUI();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  //  private void setIconEnable(Menu menu, boolean enable)
//  {
//    try
//    {
//      Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
//      Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
//      m.setAccessible(true);
//
//      //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
//      m.invoke(menu, enable);
//
//    } catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);
    MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
    if (mSubtitleVisible) {
      subtitleItem.setTitle(R.string.hide_subtitle);
    } else {
      subtitleItem.setTitle(R.string.show_subtitle);
    }
//    setIconEnable(menu,true);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.new_crime:
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = PagerViewActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
        return true;
      case R.id.show_subtitle:
        mSubtitleVisible = !mSubtitleVisible;
        getActivity().invalidateOptionsMenu();
        updateSubtitle();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(SUBTITLE_VISIBLE, mSubtitleVisible);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {

    }
  }

  private void updateUI() {
    CrimeLab crimeLab = CrimeLab.get(getActivity());
    List<Crime>crimes = crimeLab.getCrimes();
    if (mCrimeAdapter == null) {
      mCrimeAdapter = new CrimeAdapter(crimeLab.getCrimes());
      mRecyclerView.setAdapter(mCrimeAdapter);
    } else {
      mCrimeAdapter.setCrimes(crimes);
      mCrimeAdapter.notifyDataSetChanged();
    }
    updateSubtitle();
  }

  private void updateSubtitle() {
    CrimeLab crimeLab = CrimeLab.get(getActivity());
    int crimeCount = crimeLab.getCrimes().size();
    String subtitle = getString(R.string.format_subtitle, crimeCount);
    if (!mSubtitleVisible) {
      subtitle = null;
    }
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.getSupportActionBar().setSubtitle(subtitle);
  }

  private class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder> {
    private List<Crime> mCrimes;

    public CrimeAdapter(List<Crime> crimes) {
      super();
      mCrimes = crimes;
    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      View view = layoutInflater.inflate(R.layout.item_crime, parent, false);
      final CrimeViewHolder crimeViewHolder = new CrimeViewHolder(view);
      crimeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = crimeViewHolder.getAdapterPosition();
          Crime crime = mCrimes.get(position);
          Intent intent = PagerViewActivity.newIntent(getActivity(), crime.getId());
          startActivityForResult(intent, REQUEST_CODE);
        }
      });
      return crimeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder holder, int position) {
      Crime crime = mCrimes.get(position);
      holder.crimeTitle.setText(crime.getTitle());
      holder.crimeDate.setText(crime.getDate().toString());
      if (crime.isSolved()) {
        holder.isSolved.setVisibility(View.VISIBLE);
      } else {
        holder.isSolved.setVisibility(View.INVISIBLE);
      }
    }

    public void setCrimes(List<Crime> crimes) {
      mCrimes = crimes;
    }

    @Override
    public int getItemCount() {
      return mCrimes.size();
    }
  }

  static class CrimeViewHolder extends RecyclerView.ViewHolder {
    TextView crimeTitle;
    TextView crimeDate;
    View crimeItem;
    ImageView isSolved;

    public CrimeViewHolder(@NonNull View itemView) {
      super(itemView);
      crimeItem = itemView;
      crimeTitle = crimeItem.findViewById(R.id.crime_title);
      crimeDate = crimeItem.findViewById(R.id.crime_date);
      isSolved = crimeItem.findViewById(R.id.hand_cut);
    }


  }

}
