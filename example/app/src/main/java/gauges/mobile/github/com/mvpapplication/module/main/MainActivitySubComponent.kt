package gauges.mobile.github.com.mvpapplication.module.main

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()

    //TODO REMINDER: move the line below to AppModule's @Module(...) annotation
    //MainActivitySubComponent::class

    //TODO REMINDER: move this to ActivitiesInjectorFactories module
}