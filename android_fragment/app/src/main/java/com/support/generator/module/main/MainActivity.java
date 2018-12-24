package com.support.generator.module.main;

import com.support.generator.R;
import com.support.generator.base.MvpActivity;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
public class MainActivity extends MvpActivity<MainActivityContract.IMainPresenter, MainActivityContract.IMainView> implements
        MainActivityContract.IMainView {
  @Override
  public int getLayoutId() {
    return R.layout.activity_main;
  }

}