package com.common.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.common.core.R

/**
 * @author liulun
 * @version V3.0.0
 * @Description: dialog基础类
 * @date 2016/6/30 10:18
 */
abstract class SimpleDialog : Dialog {
    protected var mContentView: View? = null

    protected abstract val layoutId: Int

    /**
     * dialog动画
     *
     * @return
     */
    protected val windowAnimations: Int
        get() = 0

    @JvmOverloads constructor(context: Context, theme: Int = R.style.AppDialog) : super(context, theme) {
        init()
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        init()
    }

    private fun init() {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        mContentView = onCreateView()
        setContentView(mContentView)
        initView()
        initDialog()
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        initLoad()
    }

    /**
     * 基础配置
     */
    private fun initDialog() {
        val window = window
        val windowanimations = windowAnimations
        if (windowanimations != 0) {
            window!!.setWindowAnimations(windowanimations)
        }
        val params = window!!.attributes
        onUpdateWindowLayoutParams(params)
        window.attributes = params
        super.onWindowAttributesChanged(params)
    }

    protected fun onCreateView(): View {
        return View.inflate(context, layoutId, null)
    }

    protected fun <T : View> findTViewById(id: Int): T {
        return mContentView?.findViewById<View>(id) as T
    }

    /**
     * 初始化组件
     */
    protected fun initView() {}

    /**
     * 加载数据
     */
    protected fun initLoad() {}

    /**
     * 设置全屏宽度
     */
    fun setFullScreenWidth() {
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setFullScreenHeight() {
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * 设置全屏
     */
    fun setFullScreen() {
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * 更新系统window的layoutparams
     *
     * @param layoutParams
     */
    protected fun onUpdateWindowLayoutParams(layoutParams: WindowManager.LayoutParams) {}

    interface OnSimpleClickListener {
        fun onSimpleClick(operation: Int, data: Any)
    }
}