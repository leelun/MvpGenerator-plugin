package com.support.generator.common;

/*
 * Created by troy379 on 08.06.17.
 */
public interface IPresenter<IVIEW> {
  void onViewAttached(IVIEW iview, boolean created);

  void onViewDetached();

  void onDestroy();
}