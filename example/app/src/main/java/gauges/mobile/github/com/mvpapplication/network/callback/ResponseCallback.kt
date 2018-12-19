package gauges.mobile.github.com.mvpapplication.network.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author leellun
 * @version V1.0
 * @Description: 自定义回调
 * @date 2018/10/11 19:20
 */
abstract class ResponseCallback<T> : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onFailure()
        onFinish()
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        onSuccess(response?.body()!!)
        onFinish()
    }

    abstract fun onStart()

    abstract fun onSuccess(t: T);

    abstract fun onFailure()

    abstract fun onFinish()

}