package com.longkai.beatbox;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SoundViewModelTest {
  private BeatBox mBeatBox;
  private SoundViewModel mSoundViewModel;
  private Sound mSound;

  @Before
  public void setUp() throws Exception {
    mBeatBox = mock(BeatBox.class);
    mSound = new Sound("assetsPath");
    mSoundViewModel = new SoundViewModel(mBeatBox);
    mSoundViewModel.setSound(mSound);
  }

  @Test
  public void exposesSoundNameAsTitle(){
    assertThat(mSoundViewModel.getTitle(),is(mSound.getName()));
  }

  @Test
  public void callBeatBoxPlayOnButtonClicked(){
    mSoundViewModel.onButtonClicked();
    verify(mBeatBox).play(mSound);
  }
}