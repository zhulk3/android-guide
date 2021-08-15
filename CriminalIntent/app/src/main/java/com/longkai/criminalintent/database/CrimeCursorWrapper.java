package com.longkai.criminalintent.database;

import static com.longkai.criminalintent.database.CrimeDBSchema.*;

import java.util.Date;
import java.util.UUID;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.longkai.criminalintent.Crime;

public class CrimeCursorWrapper extends CursorWrapper {
  /**
   * Creates a cursor wrapper.
   *
   * @param cursor The underlying cursor to wrap.
   */
  public CrimeCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Crime getCrime() {
    String uuid = getString(getColumnIndex(CrimeTable.COLs.UUID));
    String title = getString(getColumnIndex(CrimeTable.COLs.TITLE));
    long date = getLong(getColumnIndex(CrimeTable.COLs.DATE));
    int isSolved = getInt(getColumnIndex(CrimeTable.COLs.SOLVED));
    Crime crime = new Crime(UUID.fromString(uuid));
    crime.setDate(new Date(date));
    crime.setSolved(isSolved != 0);
    crime.setTitle(title);
    return crime;
  }
}
