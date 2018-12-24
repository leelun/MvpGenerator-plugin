package com.support.generator.di.factories;

import android.support.v4.app.Fragment;

import com.support.generator.module.home.Home2Fragment;
import com.support.generator.module.home.Home2FragmentSubComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * @author: leellun
 * @data: 2018/12/20.
 */
@Module(subcomponents = {Home2FragmentSubComponent.class})
public abstract class FragmentsInjectorFactories {

  //TODO bind your fragments' injection factories here
  @Binds
  @IntoMap
  @FragmentKey(Home2Fragment.class)
  public abstract AndroidInjector.Factory<? extends Fragment> bindHome2FragmentInjectorFactory(Home2FragmentSubComponent.Builder builder);
}