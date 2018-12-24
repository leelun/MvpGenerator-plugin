package com.common.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author: leellun
 * @data: 2018/12/19.
 */
public abstract class SimpleDialog extends Dialog {
  protected View mContentView;

  public SimpleDialog( Context context) {
    super(context);
    init();
  }

  public SimpleDialog(Context context, int themeResId) {
    super(context, themeResId);
    init();
  }

  protected abstract int getLayoutId();

  /**
   * dialog动画
   *
   * @return
   */
  protected int getWindowAnimations(){return 0;}


  private void init() {
    setCanceledOnTouchOutside(false);
    setCancelable(false);
    mContentView = onCreateView();
    setContentView(mContentView);
    initView();
    initDialog();
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initLoad();
  }

  /**
   * 基础配置
   */
  private void initDialog() {
    Window window = getWindow();
    int windowanimations = getWindowAnimations();
    if (windowanimations != 0) {
      window.setWindowAnimations(windowanimations);
    }
    WindowManager.LayoutParams  params = window.getAttributes();
    onUpdateWindowLayoutParams(params);
    window.setAttributes(params);
    super.onWindowAttributesChanged(params);
  }

  protected View onCreateView(){
    return View.inflate(getContext(), getLayoutId(), null);
  }

  protected <T extends View> T  findTViewById(int id){
    return (T)mContentView.findViewById(id);
  }

  /**
   * 初始化组件
   */
  protected void initView() {}

  /**
   * 加载数据
   */
  protected void initLoad() {}

  /**
   * 设置全屏宽度
   */
  public void  setFullScreenWidth() {
    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  public void  setFullScreenHeight() {
    getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
  }

  /**
   * 设置全屏
   */
  public void setFullScreen() {
    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
  }

  /**
   * 更新系统window的layoutparams
   *
   * @param layoutParams
   */
  protected void onUpdateWindowLayoutParams(WindowManager.LayoutParams layoutParams) {}

  interface OnSimpleClickListener {
    void onSimpleClick(int operation, Object data);
  }
}
