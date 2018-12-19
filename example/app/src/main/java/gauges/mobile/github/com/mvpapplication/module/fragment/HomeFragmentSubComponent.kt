package gauges.mobile.github.com.mvpapplication.module.fragment

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Subcomponent(modules = arrayOf(HomeFragmentModule::class))
interface HomeFragmentSubComponent : AndroidInjector<HomeFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HomeFragment>()

    //TODO REMINDER: move the line below to FragmentsInjectorFactories's @Module(...) annotation
    //HomeFragmentSubComponent::class

    //TODO REMINDER: move this to FragmentsInjectorFactories module

}