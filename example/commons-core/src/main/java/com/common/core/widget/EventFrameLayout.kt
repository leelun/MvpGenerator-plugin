package com.common.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * @author liulun
 * @version V1.0
 * @Description: 事件拦截器
 * @date 2017/1/16 19:24
 */
class EventFrameLayout : LinearLayout {
    private var isInterceptTouchEvent = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setInterceptTouchEvent(isInterceptTouchEvent: Boolean) {
        this.isInterceptTouchEvent = isInterceptTouchEvent
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isInterceptTouchEvent
    }
}
