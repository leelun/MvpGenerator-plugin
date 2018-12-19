package gauges.mobile.github.com.mvpapplication.network

import android.text.TextUtils
import com.common.core.utils.CmLog
import com.common.core.utils.DeviceInfo
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import gauges.mobile.github.com.mvpapplication.api.IHomeApi
import gauges.mobile.github.com.mvpapplication.application.MvpApplication
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.Buffer
import java.util.concurrent.TimeUnit
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import retrofit2.ExecutorCallbackAdapterFactory


/**
 * @author leellun
 * @version V1.0
 * @Description: retrofitservice服务
 * @date 2018/10/11 17:53
 */
class RetrofitService {
    companion object {
        private lateinit var iHomeApi: IHomeApi;


        //重写request
        var rewriteControlInterceptor = Interceptor { chain ->
            val request = chain.request();
            if (!DeviceInfo.isNetworkConnected(MvpApplication.getInstance())) {
                request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }
            val response = chain.proceed(request)
            return@Interceptor response
        }
        val httpLoggingInterceptor=HttpLoggingInterceptor(object:HttpLoggingInterceptor.Logger {
            override fun log(message: String?) {
                if(!TextUtils.isEmpty(message)){
                    CmLog.w("httplog",message!!)
                }
            }
        })
        init {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor(rewriteControlInterceptor)
                    .addNetworkInterceptor(httpLoggingInterceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MvpApplication.getInstance())))
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(ExecutorCallbackAdapterFactory.create())
                    .baseUrl("http://www.tadu.com")
                    .build()
            iHomeApi = retrofit.create(IHomeApi::class.java)
        }

        fun getHomeApi(): IHomeApi {
            return iHomeApi
        }
    }
}