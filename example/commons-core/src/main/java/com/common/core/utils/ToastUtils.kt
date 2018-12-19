package com.common.core.utils

import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.common.core.application.BaseApplication

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/10 18:45
 */
object ToastUtils {
    private var mToast: Toast? = null
    private var mHandler: Handler = Handler()
    private var r: Runnable ?= object : Runnable {
        override fun run() {
            mToast?.cancel()
        }
    }
    fun show(msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        mHandler.removeCallbacks(r)
        if (mToast != null) {
            mToast?.setText(msg)
        } else {
            mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT)
        }
        mHandler.postDelayed(r, 2000)
        mToast?.show()
    }
    fun show(resId:Int){
        show(BaseApplication.getInstance()!!.resources.getString(resId))
    }
    fun destory(){
        mToast?.cancel()
        mToast=null
        mHandler.removeCallbacks(r)
        r=null
    }
}