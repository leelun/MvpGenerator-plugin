package gauges.mobile.github.com.mvpapplication.module.main

import android.content.Context
import dagger.Module
import dagger.Provides
import gauges.mobile.github.com.mvpapplication.common.mvphelper.PresenterLoader

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
@Module
class MainActivityModule {

    @Provides
    fun providePresenterLoader(context: Context, presenter: MainActivityPresenter)
            : PresenterLoader<MainActivityContract.IMainPresenter> = PresenterLoader(context, presenter)
}