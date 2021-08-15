package com.longkai.geoquiz;

public class Question {
  private int mTextResId;
  private boolean questionAnswer;

  public Question(int textResId, boolean questionAnswer) {
    mTextResId = textResId;
    this.questionAnswer = questionAnswer;
  }

  public int getTextResId() {
    return mTextResId;
  }

  public void setTextResId(int textResId) {
    mTextResId = textResId;
  }

  public boolean isQuestionAnswer() {
    return questionAnswer;
  }

  public void setQuestionAnswer(boolean questionAnswer) {
    this.questionAnswer = questionAnswer;
  }
}
