package com.support.generator.base;

import android.os.Bundle;

import com.common.core.fragment.SimpleFragment;
import com.support.generator.common.IPresenter;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author: leellun
 * @data: 2018/12/19.
 */
public abstract class MvpFragment<PRESENTER extends IPresenter<VIEW>, VIEW> extends SimpleFragment {
  @Inject
  protected PRESENTER presenter;
  private boolean firstStart = true;

  public void onCreate(Bundle savedInstanceState) {
    AndroidSupportInjection.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected final void onCreateViewFinish() {
    doStart();
  }

  private void doStart() {
    if (presenter != null) {
      presenter.onViewAttached((VIEW) this, firstStart);
    }
    firstStart = false;
  }

  public void onStart() {
    super.onStart();
    if (!firstStart) {
      doStart();
    }
  }

  @Override
  public void onDestroy() {
    if (presenter != null) {
      presenter.onDestroy();
    }
    presenter = null;
    super.onDestroy();
  }

  public void onStop() {
    super.onStop();
    if (presenter != null) presenter.onViewDetached();
  }
}
