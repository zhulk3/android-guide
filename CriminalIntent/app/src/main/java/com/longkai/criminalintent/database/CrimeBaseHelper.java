package com.longkai.criminalintent.database;

import static com.longkai.criminalintent.database.CrimeDBSchema.CrimeTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class CrimeBaseHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "crimeBase.db";

  public CrimeBaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, 1);
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table " + CrimeTable.NAME + "( _id integer primary key autoincrement," +
        CrimeTable.COLs.UUID + "," + CrimeTable.COLs.DATE + "," + CrimeTable.COLs.SOLVED + "," +
        CrimeTable.COLs.TITLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
