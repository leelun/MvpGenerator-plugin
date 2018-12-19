package com.common.core.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView

import com.common.core.R

/**
 * @author liulun
 * @version V1.0
 * @Description: shape自定义imageview
 * @date 2016/11/23 14:01
 */
class ShapeImageView : ImageView {
    private val mDrawable = GradientDrawable()

    constructor(context: Context) : super(context) {}

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.shape, defStyleAttr, 0)
        val shape = a.getInt(R.styleable.shape_shape, GradientDrawable.RECTANGLE)
        setShape(shape)
        val corners_radius = a.getFloat(R.styleable.shape_corners_radius, -1f)
        val bottomLeftRadius = a.getFloat(R.styleable.shape_corners_bottomLeftRadius, corners_radius)
        val bottomRightRadius = a.getFloat(R.styleable.shape_corners_bottomRightRadius, corners_radius)
        val topLeftRadius = a.getFloat(R.styleable.shape_corners_topLeftRadius, corners_radius)
        val topRightRadius = a.getFloat(R.styleable.shape_corners_topRightRadius, corners_radius)
        mDrawable.cornerRadii = floatArrayOf(topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius)
        val stroke_width = a.getDimension(R.styleable.shape_stroke_width, 0f).toInt()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val colorStateList = a.getColorStateList(
                    R.styleable.shape_solid_color)
            if (colorStateList != null) {
                mDrawable.color = colorStateList
            }
            val strokeColorStateList = a.getColorStateList(
                    R.styleable.shape_stroke_color)
            if (strokeColorStateList != null) {
                mDrawable.color = strokeColorStateList
            }
        } else {
            val solid_color = a.getColor(R.styleable.shape_solid_color, -1)
            if (solid_color != -1) {
                mDrawable.setColor(solid_color)
            }
            val stroke_color = a.getColor(R.styleable.shape_stroke_color, -1)
            if (stroke_color != -1) {
                mDrawable.setStroke(stroke_width, stroke_color)
            }
        }

    }

    /**
     * 设置颜色
     * @param color
     */
    fun setSolidColor(color: Int) {
        mDrawable.setColor(color)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDrawable.setBounds(0, 0, w, h)
    }

    /**
     * 设置shape
     * @param shape
     */
    private fun setShape(shape: Int) {
        when (shape) {
            GradientDrawable.RECTANGLE, GradientDrawable.OVAL, GradientDrawable.LINE, GradientDrawable.RING -> mDrawable.shape = shape
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mDrawable.draw(canvas)
    }
}
