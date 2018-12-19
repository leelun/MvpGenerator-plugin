package com.common.core.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author leellun
 * @version V1.0
 * @Description: 正方形framelayout
 * @date 2017/7/17
 */

class SquareFrameLayout : FrameLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}
