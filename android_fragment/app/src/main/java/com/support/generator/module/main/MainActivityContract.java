package com.support.generator.module.main;

import com.support.generator.common.IPresenter;
import com.support.generator.common.IView;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
public interface MainActivityContract {

  public interface IMainView extends IView {

  }

  public interface IMainPresenter extends IPresenter<IMainView> {

  }
}