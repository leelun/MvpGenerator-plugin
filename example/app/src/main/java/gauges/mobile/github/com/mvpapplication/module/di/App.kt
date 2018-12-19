package gauges.mobile.github.com.mvpapplication.module.di

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingActivityInjector
import dagger.android.support.HasDispatchingSupportFragmentInjector
import gauges.mobile.github.com.mvpapplication.module.di.components.DaggerAppComponent
import javax.inject.Inject

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
class App : Application(), HasDispatchingActivityInjector, HasDispatchingSupportFragmentInjector {

    //TODO REMINDER: register this class in AndroidManifest.xml

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingFragmentAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingAndroidInjector

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> = dispatchingFragmentAndroidInjector
}