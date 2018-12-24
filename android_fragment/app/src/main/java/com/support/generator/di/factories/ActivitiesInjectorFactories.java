package com.support.generator.di.factories;

import android.app.Activity;

import com.support.generator.module.main.MainActivity;
import com.support.generator.module.main.MainActivitySubComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author: leellun
 * @data: 2018/12/20.
 */
@Module(subcomponents = {MainActivitySubComponent.class})
public abstract class ActivitiesInjectorFactories {

  //TODO bind your activities' injection factories here
  @Binds
  @IntoMap
  @ActivityKey(MainActivity.class)
  public abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
                MainActivitySubComponent.Builder builder);
}