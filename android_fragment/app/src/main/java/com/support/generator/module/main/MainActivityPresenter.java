package com.support.generator.module.main;

import com.support.generator.common.Presenter;

import javax.inject.Inject;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
public class MainActivityPresenter
        extends Presenter<MainActivityContract.IMainView> implements MainActivityContract.IMainPresenter {
  @Inject
  public MainActivityPresenter() {
  }

  public void onViewAttached(MainActivityContract.IMainView iview, boolean created) {
    super.onViewAttached(iview, created);
  }

  public void onViewDetached() {
    super.onViewDetached();
  }

  public void onDestroy() {
    super.onDestroy();
  }
}