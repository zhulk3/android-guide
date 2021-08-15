package com.longkai.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
  private static final String DATE_ARGS = "date";
  public static final String EXTRA_DATE = "com.longkai.criminalintent.date";
  private DatePicker mDatePicker;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Date date = (Date) getArguments().getSerializable(DATE_ARGS);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
    mDatePicker = view.findViewById(R.id.dialog_date_picker);
    mDatePicker.init(year, month, day, null);
    return new AlertDialog.Builder(getActivity())
        .setView(view)
        .setTitle(R.string.date_picker_title)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth();
            int day = mDatePicker.getDayOfMonth();
            Date date_choice = new GregorianCalendar(year,month,day).getTime();
            sendResult(Activity.RESULT_OK,date_choice);
          }
        }).create();
  }

  private void sendResult(int resultCode,Date date){
    if (    getTargetFragment() ==null){
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(EXTRA_DATE,date);
    getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
  }
  public static DatePickerFragment newInstance(Date date) {
    Bundle args = new Bundle();
    args.putSerializable(DATE_ARGS, date);
    DatePickerFragment datePickerFragment = new DatePickerFragment();
    datePickerFragment.setArguments(args);
    return datePickerFragment;
  }
}
