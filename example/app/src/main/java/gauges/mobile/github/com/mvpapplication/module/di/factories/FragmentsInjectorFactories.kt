package gauges.mobile.github.com.mvpapplication.module.di.factories

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import gauges.mobile.github.com.mvpapplication.module.fragment.HomeFragment
import gauges.mobile.github.com.mvpapplication.module.fragment.HomeFragmentSubComponent

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Module(subcomponents = arrayOf(
//        register your fragments' subcomponents here
        HomeFragmentSubComponent::class
))
abstract class FragmentsInjectorFactories {

    //TODO bind your fragments' injection factories here
    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun bindHomeFragmentInjectorFactory(builder: HomeFragmentSubComponent.Builder):
            AndroidInjector.Factory<out Fragment>
}