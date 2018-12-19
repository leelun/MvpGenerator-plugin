package com.common.core.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.widget.ImageView

import com.common.core.R

/**
 * Created by DELL on 2016/11/9.
 */
class CircleImageView : ImageView {
    private var mBorderThickness = 0
    private var mContext: Context? = null
    private val defaultColor = 0
    // 如果只有其中一个有值，则只画一个圆形边框
    private var mBorderOutsideColor = 0
    private var mBorderInsideColor = 0
    // 控件默认长、宽
    private var defaultWidth = 0
    private var defaultHeight = 0

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        setCustomAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        mContext = context
        setCustomAttributes(attrs)
    }

    private fun setCustomAttributes(attrs: AttributeSet) {
        val a = mContext!!.obtainStyledAttributes(attrs, R.styleable.roundedimageview)
        mBorderThickness = a.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 0)
        mBorderOutsideColor = a.getColor(R.styleable.roundedimageview_border_outside_color, defaultColor)
        mBorderInsideColor = a.getColor(R.styleable.roundedimageview_border_inside_color, defaultColor)
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return
        if (width == 0 || height == 0) {
            return
        }
        this.measure(0, 0)
        if (drawable.javaClass == NinePatchDrawable::class.java)
            return
        val b: Bitmap?
        if (drawable is TransitionDrawable) {
            b = (drawable.getDrawable(drawable.numberOfLayers - 1) as BitmapDrawable).bitmap
        } else {
            b = (drawable as BitmapDrawable).bitmap

        }
        if (b == null) {
            return
        }
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
        if (defaultWidth == 0) {
            defaultWidth = width
        }
        if (defaultHeight == 0) {
            defaultHeight = height
        }
        var radius = 0
        if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框
            radius = (if (defaultWidth < defaultHeight) defaultWidth else defaultHeight) / 2 - 2 * mBorderThickness
            // 画内圆
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor)
            // 画外圆
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor)
        } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 定义画一个边框
            radius = (if (defaultWidth < defaultHeight) defaultWidth else defaultHeight) / 2 - mBorderThickness
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor)
        } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 定义画一个边框
            radius = (if (defaultWidth < defaultHeight) defaultWidth else defaultHeight) / 2 - mBorderThickness
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor)
        } else {// 没有边框
            radius = (if (defaultWidth < defaultHeight) defaultWidth else defaultHeight) / 2
        }
        val roundBitmap = getCroppedRoundBitmap(bitmap, radius)

        canvas.drawBitmap(roundBitmap, (defaultWidth / 2 - radius).toFloat(), (defaultHeight / 2 - radius).toFloat(), null)
    }

    /**
     * 获取裁剪后的圆形图片
     */
    fun getCroppedRoundBitmap(bmp: Bitmap?, radius: Int): Bitmap {
        var bmp = bmp
        var scaledSrcBmp: Bitmap?
        val diameter = radius * 2
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        val bmpWidth = bmp!!.width
        val bmpHeight = bmp.height
        var squareWidth = 0
        var squareHeight = 0
        var x = 0
        var y = 0
        var squareBitmap: Bitmap?
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareHeight = bmpWidth
            squareWidth = squareHeight
            x = 0
            y = (bmpHeight - bmpWidth) / 2
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight)
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareHeight = bmpHeight
            squareWidth = squareHeight
            x = (bmpWidth - bmpHeight) / 2
            y = 0
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight)
        } else {
            squareBitmap = bmp
        }
        if (squareBitmap!!.width != diameter || squareBitmap.height != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true)
        } else {
            scaledSrcBmp = squareBitmap
        }
        val output = Bitmap.createBitmap(scaledSrcBmp!!.width,
                scaledSrcBmp.height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, scaledSrcBmp.width, scaledSrcBmp.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((scaledSrcBmp.width / 2).toFloat(), (scaledSrcBmp.height / 2).toFloat(), (scaledSrcBmp.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint)
        bmp = null
        squareBitmap = null
        scaledSrcBmp = null
        return output
    }

    /**
     * 边缘画圆
     */
    private fun drawCircleBorder(canvas: Canvas, radius: Int, color: Int) {
        val paint = Paint()
        /* 去锯齿 */
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.color = color
        /* 设置paint的　style　为STROKE：空心 */
        paint.style = Paint.Style.STROKE
        /* 设置paint的外框宽度 */
        paint.strokeWidth = mBorderThickness.toFloat()
        canvas.drawCircle((defaultWidth / 2).toFloat(), (defaultHeight / 2).toFloat(), radius.toFloat(), paint)
    }
    /*private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private float mStrokeWidth;
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context) {
        super(context);
    }

    private final RectF roundRect = new RectF();
    private float rect_adius = 90;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        maskPaint.setFilterBitmap(true);
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        zonePaint.setFilterBitmap(true);
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
        mPaint.setColor(Color.WHITE);
        mStrokeWidth= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2,getContext().getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    public void setRectAdius(float adius) {
        rect_adius = adius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);

        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
        float minWidth=(float)getWidth();
        float minHeight=(float)getHeight();
        float radius=(minHeight>minWidth?minWidth:minHeight)/2-mStrokeWidth/2;
        canvas.drawCircle(minWidth/2,minHeight/2,radius,mPaint);
    }*/
}
