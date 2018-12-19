package gauges.mobile.github.com.mvpapplication.common.mvphelper

/*
 * Created by troy379 on 08.06.17.
 */
interface IPresenter<in IVIEW> {
    fun onViewAttached(iview: IVIEW, created: Boolean)
    fun onViewDetached()
    fun onDestroy()
}