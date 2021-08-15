package com.longkai.criminalintent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.longkai.criminalintent.database.CrimeBaseHelper;
import com.longkai.criminalintent.database.CrimeCursorWrapper;
import com.longkai.criminalintent.database.CrimeDBSchema.CrimeTable;

public class CrimeLab {
  private static CrimeLab sCrimeLab;
  private static final String TAG = "CrimeLab";
  private Context mContext;
  private SQLiteDatabase mSQLiteDatabase;
  private List<Crime> mCrimes;

  private CrimeLab(Context context) {
    mContext = context.getApplicationContext();
    mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
  }

  public static CrimeLab get(Context context) {
    if (sCrimeLab == null) {
      sCrimeLab = new CrimeLab(context);
    }
    return sCrimeLab;
  }

  public List<Crime> getCrimes() {
    CrimeCursorWrapper cursorWrapper = (CrimeCursorWrapper) queryCrimes(null, null);
    try {
      cursorWrapper.moveToFirst();
      while (!cursorWrapper.isAfterLast()) {
        mCrimes.add(cursorWrapper.getCrime());
        cursorWrapper.moveToNext();
      }
    } finally {
      cursorWrapper.close();
    }
    return new ArrayList<>();
  }

  public Crime getCrime(UUID id) {
    CrimeCursorWrapper cursorWrapper =
        (CrimeCursorWrapper) queryCrimes(CrimeTable.COLs.UUID + " = ?",
            new String[]{id.toString()});
    try {
      if (cursorWrapper.getCount() == 0) {
        return null;
      }
      cursorWrapper.moveToFirst();
      return cursorWrapper.getCrime();
    } finally {
      cursorWrapper.close();
    }
  }

  public void addCrime(Crime crime) {
    ContentValues contentValues = getContentValues(crime);
    mSQLiteDatabase.insert(CrimeTable.NAME, null, contentValues);
  }

  public void updateCrime(Crime crime) {
    String uuid = crime.getId().toString();
    ContentValues contentValues = getContentValues(crime);
    mSQLiteDatabase
        .update(CrimeTable.NAME, contentValues, CrimeTable.COLs.UUID + "= ?", new String[]{uuid});
  }

  public CursorWrapper queryCrimes(String whereClause, String whereArgs[]) {
    Cursor cursor =
        mSQLiteDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
    return new CrimeCursorWrapper(cursor);
  }

  private static ContentValues getContentValues(Crime crime) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(CrimeTable.COLs.UUID, crime.getId().toString());
    contentValues.put(CrimeTable.COLs.DATE, crime.getDate().toString());
    contentValues.put(CrimeTable.COLs.TITLE, crime.getTitle());
    contentValues.put(CrimeTable.COLs.SOLVED, crime.isSolved());
    return contentValues;
  }
}
