package gauges.mobile.github.com.mvpapplication.module.fragment

import gauges.mobile.github.com.mvpapplication.R
import gauges.mobile.github.com.mvpapplication.common.base.MvpFragment

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
class HomeFragment : MvpFragment<HomeFragmentContract.IHomePresenter, HomeFragmentContract.IHomeView>(),
        HomeFragmentContract.IHomeView {

    override fun getLayoutId(): Int = R.layout.fragment_home
}