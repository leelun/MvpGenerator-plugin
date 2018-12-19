package gauges.mobile.github.com.mvpapplication.module.main

import gauges.mobile.github.com.mvpapplication.common.mvphelper.IPresenter
import gauges.mobile.github.com.mvpapplication.common.mvphelper.IView

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
interface MainActivityContract {

    interface IMainView : IView {

    }

    interface IMainPresenter : IPresenter<IMainView> {

    }
}