package gauges.mobile.github.com.mvpapplication.module.di.factories

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import gauges.mobile.github.com.mvpapplication.module.main.MainActivity
import gauges.mobile.github.com.mvpapplication.module.main.MainActivitySubComponent

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Module
abstract class ActivitiesInjectorFactories {

    //TODO bind your activities' injection factories here
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(
            builder: MainActivitySubComponent.Builder): AndroidInjector.Factory<out Activity>
}