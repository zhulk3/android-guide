package com.longkai.beatbox;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
  protected abstract Fragment createFragment();//抽象类的方法需要访问修饰符。

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if(fragment==null){
      fragment = createFragment();
      fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
    }
  }
}
