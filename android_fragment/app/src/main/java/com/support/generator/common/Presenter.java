package com.support.generator.common;

import io.reactivex.disposables.CompositeDisposable;

/*
 * Created by troy379 on 08.06.17.
 */
public abstract class Presenter<IVIEW> implements IPresenter<IVIEW> {

  protected IVIEW iview;
  protected CompositeDisposable disposables = new CompositeDisposable();

  public void onViewAttached(IVIEW iview, boolean created) {
    this.iview = iview;
  }

  public void onViewDetached() {
    this.iview = null;
  }

  public void onDestroy() {
    disposables.clear();
  }
}