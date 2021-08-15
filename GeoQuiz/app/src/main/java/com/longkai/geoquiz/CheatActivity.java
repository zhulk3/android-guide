package com.longkai.geoquiz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
  private static final String EXTRA_STRING_IS_TRUE = "com.longkai.geoquiz";
  private boolean mAnswer;
  private TextView mAnswerTextView;
  private Button mShowAnswerButton;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cheat);

    mAnswerTextView = findViewById(R.id.answer_text_view);
    mShowAnswerButton = findViewById(R.id.show_answer_button);

    mAnswer = getIntent().getBooleanExtra(EXTRA_STRING_IS_TRUE, false);

    mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mAnswer) {
          mAnswerTextView.setText(R.string.correct_answer);
        } else {
          mAnswerTextView.setText(R.string.incorrect_answer);
        }
      }

      int cx = mShowAnswerButton.getWidth()/2;
      int cy = mShowAnswerButton.getHeight()/2;
      float radius = mShowAnswerButton.getWidth();
      Animator mAnimator = ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy,radius,0);
    });

  }

  public static Intent newIntent(Context context, boolean answer) {
    Intent intent = new Intent(context, CheatActivity.class);
    intent.putExtra(EXTRA_STRING_IS_TRUE, answer);
    return intent;
  }
}