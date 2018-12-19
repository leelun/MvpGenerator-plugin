package gauges.mobile.github.com.mvpapplication.module.fragment

import gauges.mobile.github.com.mvpapplication.common.mvphelper.Presenter
import javax.inject.Inject

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
class HomeFragmentPresenter @Inject constructor()
    : Presenter<HomeFragmentContract.IHomeView>(), HomeFragmentContract.IHomePresenter {

    override fun onViewAttached(view: HomeFragmentContract.IHomeView, created: Boolean) {
        super.onViewAttached(view, created)
    }

    override fun onViewDetached() {
        super.onViewDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}