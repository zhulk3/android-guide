package com.longkai.criminalintent;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class CrimeFragment extends Fragment {

  private Crime mCrime;
  private EditText mTitleField;
  private Button mDateButton;
  private CheckBox mIsCrimeSolvedButton;
  public static final String EXTRA_CRIME_ID = "com.longkai.criminalintent";
  private static final String DIALOG_DATE = "DialogDate";
  private static final int REQUEST_DATE = 0;

  public static CrimeFragment newInstance(UUID uuid) {
    Bundle args = new Bundle();
    args.putSerializable(EXTRA_CRIME_ID, uuid);
    CrimeFragment crimeFragment = new CrimeFragment();
    crimeFragment.setArguments(args);
    return crimeFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UUID uuid = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
    mCrime = CrimeLab.get(getActivity()).getCrime(uuid);
  }

  @Override
  public void onPause() {
    super.onPause();
    CrimeLab.get(getActivity()).updateCrime(mCrime);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_crime, container, false);
    mTitleField = view.findViewById(R.id.crime_title);
    mDateButton = view.findViewById(R.id.crime_date);
    mIsCrimeSolvedButton = view.findViewById(R.id.crime_solved);
    mTitleField.setText(mCrime.getTitle());
    mIsCrimeSolvedButton.setChecked(mCrime.isSolved());
    mTitleField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    updateDate();
    mDateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment alertDialog = DatePickerFragment.newInstance(mCrime.getDate());
        alertDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        alertDialog.show(fm, DIALOG_DATE);
      }
    });

    mIsCrimeSolvedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setSolved(isChecked);
      }
    });
    return view;
  }

  public void returnResult() {
    getActivity().setResult(Activity.RESULT_OK, null);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == REQUEST_DATE) {
      Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
      mCrime.setDate(date);
      updateDate();
    }
  }

  private void updateDate() {
    mDateButton.setText(mCrime.getDate().toString());
  }
}