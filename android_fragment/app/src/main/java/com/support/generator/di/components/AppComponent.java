package com.support.generator.di.components;

import com.support.generator.di.App;
import com.support.generator.di.factories.ActivitiesInjectorFactories;
import com.support.generator.di.factories.FragmentsInjectorFactories;
import com.support.generator.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author: leellun
 * @data: 2018/12/20.
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivitiesInjectorFactories.class,
        FragmentsInjectorFactories.class,
        AppModule.class
})
public interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    public Builder application(App application);

    public AppComponent build();
  }

  public void inject(App app);

}