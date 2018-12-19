package gauges.mobile.github.com.mvpapplication.common.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.util.AttributeSet
import android.view.View
import dagger.android.AndroidInjection
import gauges.mobile.github.com.mvpapplication.common.mvphelper.IPresenter
import gauges.mobile.github.com.mvpapplication.common.mvphelper.PresenterLoader
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/17 13:32
 */
abstract class MvpActivity <PRESENTER: IPresenter<IVIEW>,in IVIEW> : DefaultActivity(), LoaderManager.LoaderCallbacks<PRESENTER>{

    @Inject lateinit var presenterLoader: PresenterLoader<PRESENTER>
    protected var presenter:PRESENTER?=null
    private val needToCallStart = AtomicBoolean(false)

    override fun onCreateViewFinish() {
        supportLoaderManager.initLoader(0,Bundle.EMPTY,this).startLoading()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        if (presenter == null) needToCallStart.set(true)
        else doStart()
    }

    override fun onStop() {
        if (presenter != null) presenter?.onViewDetached()
        super.onStop()
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<PRESENTER> = presenterLoader

    override fun onLoadFinished(loader: Loader<PRESENTER>, presenter: PRESENTER) {
        this.presenter = presenter
        if (needToCallStart.compareAndSet(true, false)) {
            doStart()
        }
    }

    override fun onLoaderReset(loader: Loader<PRESENTER>) {
        presenter = null
    }
    private var firstStart: Boolean = false
    private fun doStart() {
        presenter?.onViewAttached(this as IVIEW, firstStart)
        firstStart = false
    }
}