package com.common.core.fragment

import android.content.Intent
import android.support.v4.app.Fragment

import com.common.core.R
import com.common.core.activity.BaseActivity
import com.common.core.manager.AppManager
import com.trello.rxlifecycle2.components.RxFragment

import java.io.Serializable


/**
 * 基类fragment
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月10日
 */
abstract class BaseFragment : RxFragment() {
    /**
     * @Title: toActivity
     * @Description: TODO 进入Activity
     * @param clazz
     * @param isClose 是否关闭当前activity
     */
    protected fun toActivity(clazz: Class<*>, isClose: Boolean) {
        toActivity(clazz, null, isClose)
    }

    /**
     * @Title: toActivity
     * @Description: TODO 进入Activity
     * @param clazz
     * @param map
     * @param isClose
     */
    @JvmOverloads protected fun toActivity(clazz: Class<*>?, map: Map<String, Serializable>? = null, isClose: Boolean = false) {
        if (clazz == null)
            return
        val intent = Intent(activity, clazz)
        if (map != null) {
            for (key in map.keys) {
                intent.putExtra(key, map[key])
            }
        }
        startActivity(intent)
        if (isClose)
            finishActivity()
    }

    /**
     * 界面跳转
     * @param clazz 跳转界面
     * @param requestCode 请求编码
     */
    protected fun toActivityForResult(clazz: Class<*>?, requestCode: Int) {
        if (clazz == null)
            return
        val intent = Intent(activity, clazz)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 界面跳转
     * @param clazz 跳转界面
     * @param args 传输数据 传数据格式为{{"字符串key":序列化对象},{"字符串key":序列化对象}}
     * @param requestCode 请求编码
     */
    protected fun toActivityGetResult(clazz: Class<*>?, args: Array<Array<Serializable>>?, requestCode: Int) {
        if (clazz == null)
            return
        val intent = Intent(activity, clazz)
        if (args != null) {
            for (i in args.indices) {
                intent.putExtra(args[i][0].toString(), args[i][1])
            }
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 带动画的activity启动
     */
    fun startActivityWithAnim(intent: Intent, isClose: Boolean) {
        startActivity(intent)
        activity.overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        if (isClose) {
            finishActivity()
        }
    }

    /**
     * 带动画的activity启动
     */
    fun startActivityForResultWithAnim(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        activity.overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
    }

    /**
     * 带动画的跳转
     * @param clazz
     */
    protected fun toActivityWithAnim(clazz: Class<*>) {
        toActivity(clazz, false, true)
    }

    /**
     * @Title: toActivity
     * @Description: TODO 进入Activity
     * @param clazz
     * @param isClose
     * 是否关闭当前activity
     */
    protected fun toActivity(clazz: Class<*>, isClose: Boolean, isAnim: Boolean) {
        toActivity(clazz, isClose, if (isAnim) BaseActivity.AnimationDirection.RIGHT_TO_LEFT else BaseActivity.AnimationDirection.DEFAULT)
    }

    /**
     * 动画方向
     * @param clazz
     * @param isClose
     * @param direction
     */
    protected fun toActivity(clazz: Class<*>?, isClose: Boolean, direction: BaseActivity.AnimationDirection) {
        if (clazz == null)
            return
        val intent = Intent(activity, clazz)
        startActivity(intent)
        when (direction) {
            BaseActivity.AnimationDirection.LEFT_TO_RIGHT -> activity.overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out)
            BaseActivity.AnimationDirection.RIGHT_TO_LEFT -> activity.overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        }
        if (isClose)
            finishActivity()
    }

    /**
     * 退出当前Activity
     * @author LiuLun
     * @Time 2015年11月13日下午3:19:33
     */
    protected fun finishActivity() {
        AppManager.getAppManager().finishActivity(activity)
    }
}
