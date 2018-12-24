package com.support.generator.di.modules;

import android.content.Context;

import com.support.generator.di.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author: leellun
 * @data: 2018/12/20.
 */
@Module
public class AppModule {

  @Singleton
  @Provides
  public Context provideContext(App application) {
    return application.getApplicationContext();
  }
}