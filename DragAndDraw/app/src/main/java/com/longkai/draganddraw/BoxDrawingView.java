package com.longkai.draganddraw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class BoxDrawingView extends View {
  private static final String TAG = "BoxDrawingView";
  private Box mCurrentBox;
  private List<Box> mBoxList = new ArrayList<>();
  private Paint mBoxPaint;
  private Paint mBackgroundPaint;

  public BoxDrawingView(Context context) {
    this(context, null);
  }

  public BoxDrawingView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    mBoxPaint = new Paint();
    mBoxPaint.setColor(0x22ff0000);
    mBackgroundPaint = new Paint();
    mBackgroundPaint.setColor(0xfff8efe0);
  }

  @Nullable
  @Override
  protected Parcelable onSaveInstanceState() {
    return super.onSaveInstanceState();
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    super.onRestoreInstanceState(state);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    PointF current = new PointF(event.getX(), event.getY());
    String action = "";
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        action = "ACTION_DOWN";
        mCurrentBox = new Box(current);
        mBoxList.add(mCurrentBox);
        break;
      case MotionEvent.ACTION_MOVE:
        action = "ACTION_MOVE";
        if (mCurrentBox != null) {
          mCurrentBox.setCurrent(current);
          invalidate();
        }
        break;
      case MotionEvent.ACTION_UP:
        action = "ACTION_UP";
        mCurrentBox = null;
        break;
      case MotionEvent.ACTION_CANCEL:
        action = "ACTION_CANCEL";
        mCurrentBox = null;
        break;
    }
    Log.d(TAG, action + " at X=" + current.x + ",y=" + current.y);
    return true;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPaint(mBackgroundPaint);
    for (Box box : mBoxList) {
      float left = Math.min(box.getOrigin().x, box.getCurrent().x);
      float right = Math.max(box.getOrigin().x, box.getCurrent().x);
      float bottom = Math.min(box.getOrigin().y, box.getCurrent().y);
      float top = Math.max(box.getOrigin().y, box.getCurrent().y);
      canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }
  }
}
