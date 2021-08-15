package com.longkai.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
  private UUID mId;
  private String mTitle;
  private Date mDate;
  private boolean isSolved;

  public Crime() {
    this(UUID.randomUUID());
  }

  public Crime(UUID id) {
    mId = UUID.randomUUID();
    mDate = new Date();
  }

  public UUID getId() {
    return mId;
  }

  public String getTitle() {
    return mTitle;
  }

  public Date getDate() {
    return mDate;
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public void setDate(Date date) {
    mDate = date;
  }

  public void setSolved(boolean solved) {
    isSolved = solved;
  }

  public boolean isSolved() {
    return isSolved;
  }
}
