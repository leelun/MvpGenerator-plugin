package gauges.mobile.github.com.mvpapplication.module.di.components

import gauges.mobile.github.com.mvpapplication.module.di.App
import gauges.mobile.github.com.mvpapplication.module.di.factories.ActivitiesInjectorFactories
import gauges.mobile.github.com.mvpapplication.module.di.factories.FragmentsInjectorFactories
import gauges.mobile.github.com.mvpapplication.module.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ActivitiesInjectorFactories::class,
        FragmentsInjectorFactories::class,
        AppModule::class
))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}