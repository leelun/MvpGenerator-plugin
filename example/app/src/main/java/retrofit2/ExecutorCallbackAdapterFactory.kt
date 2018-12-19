package retrofit2

import gauges.mobile.github.com.mvpapplication.network.callback.ResponseCallback
import okhttp3.Request
import retrofit2.Utils.checkNotNull
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/12 0:20
 */
class ExecutorCallbackAdapterFactory internal constructor(internal val callbackExecutor: Executor) : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (CallAdapter.Factory.getRawType(returnType) != Call::class.java) {
            return null
        }
        val responseType = Utils.getCallResponseType(returnType)
        return object : CallAdapter<Any, Call<*>> {
            override fun responseType(): Type? {
                return responseType
            }

            override fun adapt(call: Call<Any>): Call<Any>? {
                return ExecutorCallbackCall2(callbackExecutor, call)
            }
        }
    }

    internal class ExecutorCallbackCall2<T>(val callbackExecutor: Executor, val delegate: Call<T>) : Call<T> {

        override fun enqueue(callback: Callback<T>) {
            checkNotNull(callback, "callback == null")
            if(callback is ResponseCallback){
                callbackExecutor.execute{
                    var responseCallback: ResponseCallback<T>?=callback
                    responseCallback?.onStart()
                }
            }
            delegate.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    callbackExecutor.execute {
                        if (delegate.isCanceled) {
                            // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
                            callback.onFailure(this@ExecutorCallbackCall2, IOException("Canceled"))
                        } else {
                            callback.onResponse(this@ExecutorCallbackCall2, response)
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    callbackExecutor.execute { callback.onFailure(this@ExecutorCallbackCall2, t) }
                }
            })
        }

        override fun isExecuted(): Boolean {
            return delegate.isExecuted
        }

        @Throws(IOException::class)
        override fun execute(): Response<T> {
            return delegate.execute()
        }

        override fun cancel() {
            delegate.cancel()
        }

        override fun isCanceled(): Boolean {
            return delegate.isCanceled
        }

        override// Performing deep clone.
        fun clone(): Call<T> {
            return ExecutorCallAdapterFactory.ExecutorCallbackCall(callbackExecutor, delegate.clone())
        }

        override fun request(): Request {
            return delegate.request()
        }
    }

    companion object {

        fun create(): ExecutorCallbackAdapterFactory {
            return ExecutorCallbackAdapterFactory(Platform.get().defaultCallbackExecutor()!!)
        }
    }
}