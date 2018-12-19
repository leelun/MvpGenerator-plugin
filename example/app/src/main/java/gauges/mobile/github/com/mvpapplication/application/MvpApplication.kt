package gauges.mobile.github.com.mvpapplication.application

import android.content.Context
import com.common.core.application.BaseApplication

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/11 18:48
 */
class MvpApplication: BaseApplication() {
    companion object {
        fun getInstance(): Context {
            return BaseApplication.getInstance()
        }
    }
    override fun onCreate() {
        super.onCreate()
    }
}
