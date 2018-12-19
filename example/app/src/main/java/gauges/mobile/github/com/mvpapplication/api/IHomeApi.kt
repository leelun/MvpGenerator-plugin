package gauges.mobile.github.com.mvpapplication.api

import gauges.mobile.github.com.mvpapplication.model.UserInfo
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/11 19:09
 */
interface IHomeApi {
    @GET("/user/info")
    fun getHomePage():Call<UserInfo>
}