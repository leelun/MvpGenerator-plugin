package gauges.mobile.github.com.mvpapplication.module.fragment

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
class HomeFragmentModule {

    @Provides
    fun providePresenterLoader(context: Context, presenter: HomeFragmentPresenter)
            : PresenterLoader<HomeFragmentContract.IHomePresenter> = PresenterLoader(context, presenter)
}