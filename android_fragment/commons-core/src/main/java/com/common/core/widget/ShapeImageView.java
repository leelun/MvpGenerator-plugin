package com.common.core.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.common.core.R;

/**
 * @author liulun
 * @version V1.0
 * @Description: shape自定义imageview
 * @date 2016/11/23 14:01
 */
public class ShapeImageView extends AppCompatImageView {
    private GradientDrawable mDrawable=new GradientDrawable();
    public ShapeImageView(Context context) {
        super(context);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.shape, defStyleAttr, 0);
        int shape=a.getInt(R.styleable.shape_shape,GradientDrawable.RECTANGLE);
        setShape(shape);
        float corners_radius=a.getFloat(R.styleable.shape_corners_radius,-1);
        float bottomLeftRadius=a.getFloat(R.styleable.shape_corners_bottomLeftRadius,corners_radius);
        float bottomRightRadius=a.getFloat(R.styleable.shape_corners_bottomRightRadius,corners_radius);
        float topLeftRadius=a.getFloat(R.styleable.shape_corners_topLeftRadius,corners_radius);
        float topRightRadius=a.getFloat(R.styleable.shape_corners_topRightRadius,corners_radius);
        mDrawable.setCornerRadii(new float[]{topLeftRadius,topLeftRadius,topRightRadius,topRightRadius,bottomRightRadius,bottomRightRadius,bottomLeftRadius,bottomLeftRadius});
        int stroke_width=(int)a.getDimension(R.styleable.shape_stroke_width,0);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT_WATCH){
            ColorStateList colorStateList = a.getColorStateList(
                    R.styleable.shape_solid_color);
            if(colorStateList!=null){
                mDrawable.setColor(colorStateList);
            }
            ColorStateList strokeColorStateList = a.getColorStateList(
                    R.styleable.shape_stroke_color);
            if(strokeColorStateList!=null){
                mDrawable.setColor(strokeColorStateList);
            }
        }else{
            int solid_color=a.getColor(R.styleable.shape_solid_color,-1);
            if(solid_color!=-1){
                mDrawable.setColor(solid_color);
            }
            int stroke_color=a.getColor(R.styleable.shape_stroke_color,-1);
            if(stroke_color!=-1){
                mDrawable.setStroke(stroke_width,stroke_color);
            }
        }

    }

    /**
     * 设置颜色
     * @param color
     */
    public void setSolidColor(int color){
        mDrawable.setColor(color);
        invalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawable.setBounds(0,0,w,h);
    }

    /**
     * 设置shape
     * @param shape
     */
    private void  setShape(int shape){
        switch(shape){
            case GradientDrawable.RECTANGLE:
            case GradientDrawable.OVAL:
            case GradientDrawable.LINE:
            case GradientDrawable.RING:
                mDrawable.setShape(shape);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.draw(canvas);
    }
}
