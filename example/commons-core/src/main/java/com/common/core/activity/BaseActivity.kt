package com.common.core.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.common.core.R
import com.common.core.manager.AppManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * 基础activity
 *
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月9日
 */
abstract class BaseActivity : RxAppCompatActivity() {

    /**
     * @return 当前activity
     * @Time 2015年12月7日下午5:56:27
     */
    protected val activity: Activity
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.getAppManager().addActivity(this)
    }

    override fun onDestroy() {
        AppManager.getAppManager().removeActivity(this)
        super.onDestroy()
    }

    /**
     * 退出当前Activity
     *
     * @author LiuLun
     * @Time 2015年11月13日下午3:19:33
     */
    protected fun finishActivity() {
        AppManager.getAppManager().finishActivity(this)
    }

    protected fun finishActivity(resultCode: Int, intent: Intent) {
        setResult(resultCode, intent)
        finishActivity()
    }
    /**
     * 带动画的跳转
     *
     * @param clazz
     */
    protected fun toActivityWithAnim(clazz: Class<*>) {
        toActivity(clazz, false, true)
    }

    /**
     * @param clazz
     * @param isClose 是否关闭当前activity
     * @Title: toActivity
     * @Description: TODO 进入Activity
     */
    @JvmOverloads protected fun toActivity(clazz: Class<*>, isClose: Boolean = false, isAnim: Boolean = false) {
        toActivity(clazz, isClose, if (isAnim) AnimationDirection.RIGHT_TO_LEFT else AnimationDirection.DEFAULT)
    }
    /**
     * 动画方向
     *
     * @param clazz
     * @param isClose
     * @param direction
     */
    protected fun toActivity(clazz: Class<*>?, isClose: Boolean, direction: AnimationDirection) {
        if (clazz == null)
            return
        val intent = Intent(this, clazz)
        startActivity(intent)
        when (direction) {
            BaseActivity.AnimationDirection.LEFT_TO_RIGHT -> overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out)
            BaseActivity.AnimationDirection.RIGHT_TO_LEFT -> overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        }
        if (isClose)
            finishActivity()
    }

    /**
     * 动画方向
     *
     * @param clazz
     * @param requestCode
     */
    protected fun toActivityForResultWithAnim(clazz: Class<*>, requestCode: Int) {
        toActivityForResult(clazz, requestCode, AnimationDirection.RIGHT_TO_LEFT)
    }

    /**
     * 动画方向
     *
     * @param clazz
     * @param requestCode
     * @param direction
     */
    protected fun toActivityForResult(clazz: Class<*>?, requestCode: Int, direction: AnimationDirection) {
        if (clazz == null)
            return
        val intent = Intent(this, clazz)
        startActivityForResult(intent, requestCode)
        when (direction) {
            BaseActivity.AnimationDirection.LEFT_TO_RIGHT -> overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out)
            BaseActivity.AnimationDirection.RIGHT_TO_LEFT -> overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        }
    }

    /**
     * 带动画的activity启动
     */
    fun startActivityWithAnim(intent: Intent, isClose: Boolean) {
        startActivity(intent)
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        if (isClose) {
            finishActivity()
        }
    }

    /**
     * 带动画的activity启动
     */
    fun startActivityForResultWithAnim(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
    }

    /**
     * 带动画的finish
     */
    protected fun finishWithAnim() {
        finishActivity()
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out)
    }

    protected fun finishActivity(direction: AnimationDirection) {
        finishActivity()
        when (direction) {
            BaseActivity.AnimationDirection.LEFT_TO_RIGHT -> overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out)
            BaseActivity.AnimationDirection.RIGHT_TO_LEFT -> overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out)
        }
    }

    enum class AnimationDirection {
        DEFAULT, LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }
}