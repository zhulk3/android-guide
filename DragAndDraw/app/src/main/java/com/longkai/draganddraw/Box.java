package com.longkai.draganddraw;

import java.io.Serializable;

import android.graphics.PointF;
import android.os.Parcelable;

public class Box implements Serializable {
  private PointF mOrigin;
  private PointF mCurrent;

  public Box(PointF origin) {
    mOrigin = origin;
  }

  public PointF getOrigin() {
    return mOrigin;
  }

  public void setOrigin(PointF origin) {
    mOrigin = origin;
  }

  public PointF getCurrent() {
    return mCurrent;
  }

  public void setCurrent(PointF current) {
    mCurrent = current;
  }
}
