package gauges.mobile.github.com.mvpapplication.common.mvphelper

import io.reactivex.disposables.CompositeDisposable

/*
 * Created by troy379 on 08.06.17.
 */
abstract class Presenter<IVIEW> : IPresenter<IVIEW> {

    protected var iview: IVIEW? = null
    protected val disposables = CompositeDisposable()

    override fun onViewAttached(iview: IVIEW, created: Boolean) {
        this.iview = iview
    }

    override fun onViewDetached() {
        this.iview = null
    }

    override fun onDestroy() = disposables.clear()
}