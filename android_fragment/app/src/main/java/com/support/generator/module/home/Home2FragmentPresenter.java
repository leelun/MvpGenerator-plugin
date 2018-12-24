package com.support.generator.module.home;

import com.support.generator.common.Presenter;

import javax.inject.Inject;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
public class Home2FragmentPresenter
        extends Presenter<Home2FragmentContract.IHome2View> implements Home2FragmentContract.IHome2Presenter {
  @Inject
  public Home2FragmentPresenter() {
  }

  public void onViewAttached(Home2FragmentContract.IHome2View view, boolean created) {
    super.onViewAttached(view, created);
  }

  public void onViewDetached() {
    super.onViewDetached();
  }

  public void onDestroy() {
    super.onDestroy();
  }
}