package com.support.generator.base;

import android.os.Bundle;

import com.support.generator.common.IPresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * @author: leellun
 * @data: 2018/12/19.
 */
public abstract class MvpActivity<PRESENTER extends IPresenter<IVIEW>, IVIEW> extends DefaultActivity {

  @Inject
  protected PRESENTER presenter;

  public final void onCreateViewFinish() {
    doStart();
  }

  public void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }

  public void onStart() {
    super.onStart();
    if (!firstStart) {
      doStart();
    }
  }

  public void onStop() {
    if (presenter != null) presenter.onViewDetached();
    super.onStop();
  }

  @Override
  public void onDestroy() {
    presenter.onDestroy();
    presenter = null;
    super.onDestroy();
  }

  private boolean firstStart = true;

  private void doStart() {
    presenter.onViewAttached((IVIEW) this, firstStart);
    firstStart = false;
  }
}
