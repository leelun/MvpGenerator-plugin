package com.support.generator.module.home;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * @author: leellun
 * @data: 2018/12/24.
 */
@Subcomponent(modules = {Home2FragmentModule.class})
public interface Home2FragmentSubComponent extends AndroidInjector<Home2Fragment> {

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<Home2Fragment> {
  }

  //TODO REMINDER: move the line below to FragmentsInjectorFactories's @Module(...) annotation
  //Home2FragmentSubComponent.class

  //TODO REMINDER: move this to FragmentsInjectorFactories module

}