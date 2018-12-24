package com.support.generator.module.home;

import com.support.generator.R;
import com.support.generator.base.MvpFragment;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
public class Home2Fragment extends MvpFragment<Home2FragmentContract.IHome2Presenter, Home2FragmentContract.IHome2View> implements Home2FragmentContract.IHome2View {

  public int getLayoutId() {
    return R.layout.fragment_home2;
  }

  @Override
  protected void initLoad() {

  }
}