package com.support.generator.module.main;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
@Module
public class MainActivityModule {

  @Provides
  public MainActivityContract.IMainPresenter provideMainPresenter() {
    return new MainActivityPresenter();
  }
}