package com.support.generator.module.home;

import dagger.Module;
import dagger.Provides;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
@Module
public class Home2FragmentModule {

  @Provides
  public Home2FragmentContract.IHome2Presenter providePresenterLoader() {
    return new Home2FragmentPresenter();
  }
}