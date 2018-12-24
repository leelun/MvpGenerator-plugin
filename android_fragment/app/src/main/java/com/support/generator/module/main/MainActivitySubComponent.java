package com.support.generator.module.main;

import android.app.Activity;

import dagger.Binds;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<MainActivity> {
  }

  //TODO REMINDER: move the line below to AppModule's @Module(...) annotation
  //MainActivitySubComponent.class
  //TODO REMINDER: move this to ActivitiesInjectorFactories module

}