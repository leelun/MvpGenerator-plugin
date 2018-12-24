package com.common.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.R;

/**
 * Created by liulun on 2016/11/14.
 * actionBar封装类
 */
public class ActionBarContainer extends FrameLayout {
  private TextView mTitleTextView;
  private FrameLayout mLeftContainer;
  private ImageView mLeftImageView;
  private Button mLeftButton;
  private LinearLayout mRightContainer;
  private ImageButton mRightImageBtn;
  private ImageView mRightImageView;
  private TextView mRightBtn;

  private int mHoldPaddingTop;

  public ActionBarContainer(Context context) {
    this(context, null);
  }

  public ActionBarContainer(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ActionBarContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    View.inflate(getContext(), R.layout.view_actionbar_container, this);
    mTitleTextView = (TextView) findViewById(R.id.title_textview);
    mLeftButton = (Button) findViewById(R.id.left_btn);
    mLeftContainer = (FrameLayout) findViewById(R.id.left_container_layout);
    mLeftImageView = (ImageView) findViewById(R.id.left_imageview);
    mRightContainer = (LinearLayout) findViewById(R.id.right_container_layout);
    mRightImageBtn = (ImageButton) findViewById(R.id.right_image_btn);
    mRightImageView = (ImageView) findViewById(R.id.right_imageview);
    mRightBtn = (TextView) findViewById(R.id.right_btn);
  }

  public void setPaddingTop(int paddingTop) {
    mHoldPaddingTop = paddingTop;
    getChildAt(0).setPadding(0, paddingTop, 0, 0);
    getChildAt(0).getLayoutParams().height += paddingTop;
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
    if (heightMode == View.MeasureSpec.EXACTLY && mHoldPaddingTop > 0) {
      int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
      heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize + mHoldPaddingTop, View.MeasureSpec.EXACTLY);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void setTitle(CharSequence title) {
    mTitleTextView.setText(title);
  }

  public void setLeftImage(int resId, OnClickListener onClickListener) {
    showLeftView(mLeftImageView);
    mLeftImageView.setImageResource(resId);
    mLeftContainer.setOnClickListener(onClickListener);
  }

  public void setLeftOnClickListener(OnClickListener onClickListener) {
    showLeftView(mLeftImageView);
    mLeftContainer.setOnClickListener(onClickListener);
  }

  public void setLeftButton(String text, OnClickListener onClickListener) {
    showLeftView(mLeftButton);
    mLeftButton.setText(text);
    mLeftContainer.setOnClickListener(onClickListener);
    mLeftButton.setOnClickListener(onClickListener);
  }

  public void setRightImageBtn(int resId, OnClickListener onClickListener) {
    showRightView(mRightImageBtn);
    mRightImageBtn.setImageResource(resId);
    mRightImageBtn.setOnClickListener(onClickListener);
    mRightContainer.setOnClickListener(onClickListener);
  }

  public void setRightImageView(int resId, OnClickListener onClickListener) {
    showRightView(mRightImageView);
    mRightImageView.setImageResource(resId);
    mRightImageView.setOnClickListener(onClickListener);
    mRightContainer.setOnClickListener(onClickListener);
  }

  public void setLeftVisible(boolean visible) {
    mLeftContainer.setVisibility(visible ? VISIBLE : GONE);
  }

  public boolean isLeftVisible() {
    return mLeftContainer.getVisibility() == VISIBLE;
  }

  public void setRightVisible(boolean visible) {
    mRightContainer.setVisibility(visible ? VISIBLE : GONE);
  }

  /**
   * 按钮设置
   *
   * @param text
   */
  public void setRightBtn(String text) {
    setRightBtn(text, null);
  }

  public void setRightBtn(String text, OnClickListener onClickListener) {
    showRightView(mRightBtn);
    mRightBtn.setText(text);
    mRightBtn.setOnClickListener(onClickListener);
    mRightContainer.setOnClickListener(onClickListener);
  }

  private void showRightView(View rightView) {
    mRightContainer.setVisibility(VISIBLE);
    for (int i = 0; i < mRightContainer.getChildCount(); i++) {
      mRightContainer.getChildAt(i).setVisibility(GONE);
    }
    rightView.setVisibility(VISIBLE);
  }

  private void showLeftView(View leftView) {
    mLeftContainer.setVisibility(VISIBLE);
    for (int i = 0; i < mLeftContainer.getChildCount(); i++) {
      mLeftContainer.getChildAt(i).setVisibility(GONE);
    }
    leftView.setVisibility(VISIBLE);
  }

}

