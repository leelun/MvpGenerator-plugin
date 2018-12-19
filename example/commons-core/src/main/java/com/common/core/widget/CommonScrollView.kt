package com.common.core.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/1/8
 */

class CommonScrollView : ScrollView {
    private var onScrollListener: OnScrollListener? = null

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.onScrollListener = onScrollListener
    }

    /**
     * 滑动监听
     */
    interface OnScrollListener {
        fun onScroll(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScrollListener != null) onScrollListener!!.onScroll(l, t, oldl, oldt)
    }
}
