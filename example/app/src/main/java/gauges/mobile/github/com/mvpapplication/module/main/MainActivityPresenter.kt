package gauges.mobile.github.com.mvpapplication.module.main

import gauges.mobile.github.com.mvpapplication.common.mvphelper.Presenter
import javax.inject.Inject

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
class MainActivityPresenter @Inject constructor()
    : Presenter<MainActivityContract.IMainView>(), MainActivityContract.IMainPresenter {

    override fun onViewAttached(iview: MainActivityContract.IMainView, created: Boolean) {
        super.onViewAttached(iview, created)
    }

    override fun onViewDetached() {
        super.onViewDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}