package com.longkai.geoquiz;

import static com.longkai.geoquiz.R.string.*;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


  private Button mCorrectButton;
  private Button mIncorrectButton;
  private boolean mAnswerIsTrue;
  private TextView mQuestionTextView;
  private int mCurrentQuestionIndex = 0;
  private Button mNextButton;
  private Button mPrevButton;
  private Button mCheatButton;
  private Question[] mQuestions = new Question[]{
      new Question(question_one, true),
      new Question(question_two, false),
      new Question(question_three, true)
  };
  private static final String TAG = "MainActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: called");
    setContentView(R.layout.activity_main);
    mCorrectButton = findViewById(R.id.correct_button);
    mIncorrectButton = findViewById(R.id.incorrect_button);
    mNextButton = findViewById(R.id.next_question);
    mQuestionTextView = findViewById(R.id.question_text);
    mPrevButton = findViewById(R.id.prev_question);

    mCorrectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(true);
      }
    });

    mIncorrectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(false);
      }
    });

    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateQuestion();
      }
    });

    mPrevButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentQuestionIndex = (mCurrentQuestionIndex + mQuestions.length - 1) % mQuestions.length;
        Log.d(TAG, "onClick: mCurrentQuestionIndex = "+mCurrentQuestionIndex);
        updateQuestion();
      }
    });

    mCheatButton=findViewById(R.id.cheat_button);

    mCheatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = CheatActivity.newIntent(MainActivity.this,mAnswerIsTrue);
        startActivity(intent);
      }
    });

    updateQuestion();
  }

  private void updateQuestion() {
//    Log.d(TAG, "updateQuestion: ",new Throwable());
    int question = mQuestions[mCurrentQuestionIndex].getTextResId();
    mAnswerIsTrue = mQuestions[mCurrentQuestionIndex].isQuestionAnswer();
    mQuestionTextView.setText(question);
  }

  private void checkAnswer(boolean selection) {
    boolean answer = mQuestions[mCurrentQuestionIndex].isQuestionAnswer();
    int messageId = 0;
    if (selection == answer) {
      messageId = correct_answer;
    } else {
      messageId = incorrect_answer;
    }
    Toast.makeText(MainActivity.this, messageId, Toast.LENGTH_SHORT);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart: called");
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: called");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause: called");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop: called");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: called");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart: called");
  }
}