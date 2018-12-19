package gauges.mobile.github.com.mvpapplication.module.main

import android.os.Bundle
import gauges.mobile.github.com.mvpapplication.R
import gauges.mobile.github.com.mvpapplication.common.base.MvpActivity
import gauges.mobile.github.com.mvpapplication.module.fragment.HomeFragment

/**
 * @author: leellun
 * @data: 2018/12/19.
 *
 */
class MainActivity : MvpActivity<MainActivityContract.IMainPresenter, MainActivityContract.IMainView>(),
        MainActivityContract.IMainView {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(R.id.layout,HomeFragment()).commit();
    }
}