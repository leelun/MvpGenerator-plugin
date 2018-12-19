package gauges.mobile.github.com.mvpapplication.model

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/11 23:54
 */
class UserInfo {
    /**
     * status : 200
     * msg : null
     * redirectUrl : null
     * data : {"mediaUrl":"http://media6.tadu.com/","staticUrl":"http://media6.tadu.com/web_dubbo_static/","isAuthor":false}
     */

    var status: Int = 0
    var msg: Any? = null
    var redirectUrl: Any? = null
    var data: DataBean? = null

    class DataBean {
        /**
         * mediaUrl : http://media6.tadu.com/
         * staticUrl : http://media6.tadu.com/web_dubbo_static/
         * isAuthor : false
         */

        var mediaUrl: String? = null
        var staticUrl: String? = null
        var isIsAuthor: Boolean = false
    }
}
