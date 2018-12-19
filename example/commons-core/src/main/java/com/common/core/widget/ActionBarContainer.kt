package com.common.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.common.core.R
import com.common.core.utils.ScreenUtils

/**
 * Created by liulun on 2016/11/14.
 * actionBar封装类
 */
class ActionBarContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var mTitleTextView: TextView? = null
    private var mLeftContainer: FrameLayout? = null
    private var mLeftImageView: ImageView? = null
    private var mLeftButton: Button? = null
    private var mRightContainer: LinearLayout? = null
    private var mRightImageBtn: ImageButton? = null
    private var mRightImageView: ImageView? = null
    private var mRightBtn: TextView? = null

    private var mHoldPaddingTop: Int = 0

    var isLeftVisible: Boolean
        get() = mLeftContainer!!.visibility == View.VISIBLE
        set(visible) {
            mLeftContainer!!.visibility = if (visible) View.VISIBLE else View.GONE
        }

    init {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_actionbar_container, this)
        mTitleTextView = findViewById(R.id.title_textview)
        mLeftButton = findViewById(R.id.left_btn)
        mLeftContainer = findViewById(R.id.left_container_layout)
        mLeftImageView = findViewById(R.id.left_imageview)
        mRightContainer = findViewById(R.id.right_container_layout)
        mRightImageBtn = findViewById(R.id.right_image_btn)
        mRightImageView = findViewById(R.id.right_imageview)
        mRightBtn = findViewById(R.id.right_btn)
    }

    fun setPaddingTop(paddingTop: Int) {
        mHoldPaddingTop = paddingTop
        getChildAt(0).setPadding(0, paddingTop, 0, 0)
        getChildAt(0).layoutParams.height += paddingTop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == View.MeasureSpec.EXACTLY && mHoldPaddingTop > 0) {
            val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize + mHoldPaddingTop, View.MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setTitle(title: CharSequence) {
        mTitleTextView!!.text = title
    }

    fun setLeftImage(resId: Int, onClickListener: View.OnClickListener) {
        showLeftView(mLeftImageView)
        mLeftImageView!!.setImageResource(resId)
        mLeftContainer!!.setOnClickListener(onClickListener)
    }

    fun setLeftOnClickListener(onClickListener: View.OnClickListener) {
        showLeftView(mLeftImageView)
        mLeftContainer!!.setOnClickListener(onClickListener)
    }

    fun setLeftButton(text: String, onClickListener: View.OnClickListener) {
        showLeftView(mLeftButton)
        mLeftButton!!.text = text
        mLeftContainer!!.setOnClickListener(onClickListener)
        mLeftButton!!.setOnClickListener(onClickListener)
    }

    fun setRightImageBtn(resId: Int, onClickListener: View.OnClickListener) {
        showRightView(mRightImageBtn)
        mRightImageBtn!!.setImageResource(resId)
        mRightImageBtn!!.setOnClickListener(onClickListener)
        mRightContainer!!.setOnClickListener(onClickListener)
    }

    fun setRightImageView(resId: Int, onClickListener: View.OnClickListener) {
        showRightView(mRightImageView)
        mRightImageView!!.setImageResource(resId)
        mRightImageView!!.setOnClickListener(onClickListener)
        mRightContainer!!.setOnClickListener(onClickListener)
    }

    fun setRightVisible(visible: Boolean) {
        mRightContainer!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmOverloads
    fun setRightBtn(text: String, onClickListener: View.OnClickListener? = null) {
        showRightView(mRightBtn)
        mRightBtn!!.text = text
        mRightBtn!!.setOnClickListener(onClickListener)
        mRightContainer!!.setOnClickListener(onClickListener)
    }

    private fun showRightView(rightView: View?) {
        mRightContainer!!.visibility = View.VISIBLE
        for (i in 0 until mRightContainer!!.childCount) {
            mRightContainer!!.getChildAt(i).visibility = View.GONE
        }
        rightView!!.visibility = View.VISIBLE
    }

    private fun showLeftView(leftView: View?) {
        mLeftContainer!!.visibility = View.VISIBLE
        for (i in 0 until mLeftContainer!!.childCount) {
            mLeftContainer!!.getChildAt(i).visibility = View.GONE
        }
        leftView!!.visibility = View.VISIBLE
    }
}
/**
 * 按钮设置
 *
 * @param text
 */
