package gauges.mobile.github.com.mvpapplication.common.base

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import gauges.mobile.github.com.mvpapplication.common.mvphelper.IPresenter
import gauges.mobile.github.com.mvpapplication.common.mvphelper.PresenterLoader
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
abstract class MvpFragment<PRESENTER: IPresenter<VIEW>,in VIEW>: DaggerFragment(),LoaderManager.LoaderCallbacks<PRESENTER> {
    protected abstract fun getLayoutId():Int
    @Inject lateinit var presenterLoader:PresenterLoader<PRESENTER>
    protected var presenter:PRESENTER?=null

    private var firstStart:Boolean=false;
    private  val needToCallStart=AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstStart=true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayoutId(),null)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, Bundle.EMPTY,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<PRESENTER> =presenterLoader
    override fun onLoadFinished(loader: Loader<PRESENTER>, data: PRESENTER) {
        this.presenter=data
        if(needToCallStart.compareAndSet(true,false)){
            doStart();
        }
    }

    override fun onStart() {
        super.onStart()
        if(presenter!=null)needToCallStart.set(true)
        else doStart()
    }

    override fun onStop() {
        if(presenter!=null)presenter?.onViewDetached()
        super.onStop()
    }

    override fun onLoaderReset(loader: Loader<PRESENTER>) {
        presenter=null
    }

    private fun doStart(){
        presenter?.onViewAttached(this as VIEW,firstStart)
        firstStart=false
    }
}