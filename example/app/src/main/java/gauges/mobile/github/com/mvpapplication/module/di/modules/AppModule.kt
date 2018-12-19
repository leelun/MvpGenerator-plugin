package gauges.mobile.github.com.mvpapplication.module.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import gauges.mobile.github.com.mvpapplication.module.di.App
import gauges.mobile.github.com.mvpapplication.module.main.MainActivitySubComponent
import javax.inject.Singleton

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Module(subcomponents = arrayOf(
//        register your activities' subcomponents here
        MainActivitySubComponent::class
))
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: App): Context = application.applicationContext
}