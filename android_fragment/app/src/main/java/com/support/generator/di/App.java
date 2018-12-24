package com.support.generator.di;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.support.generator.di.components.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author: leellun
 * @data: 2018/12/20.
 */
public class App extends Application implements HasActivityInjector, HasSupportFragmentInjector {

//TODO REMINDER: register this class in AndroidManifest.xml

  @Inject
  DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
  @Inject
  DispatchingAndroidInjector<Fragment> dispatchingFragmentAndroidInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this);
  }

  public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingAndroidInjector;
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentAndroidInjector;
  }
}