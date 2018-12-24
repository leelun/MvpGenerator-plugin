package com.common.core.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.common.core.utils.ToastUtils;


/**
 * @version V3.0.0
 * @Description: SimpleFragment is default fragment
 * @date 2016/9/13 15:46
 */
public abstract class SimpleFragment extends BaseFragment {
  private View mRootView;
  private boolean mIsVisibleToUser;
  private boolean mIsUserVisibleHint;
  private boolean mIsData = false;

  /**
   * 获取fragment布局
   *
   * @return fragment布局id
   * @author LiuLun
   * @Time 2015年11月10日上午10:26:34
   */
  public abstract int getLayoutId();

  /**
   * @return whether continue find views
   * @Description initialize data
   */
  protected boolean initData() {
    return true;
  }

  /**
   * use with {@link #getLayoutId}
   */
  protected void initViews() {
  }

  ;

  /**
   * load data
   */
  protected abstract void initLoad();

  /**
   * 是否使用uservisiblehint
   *
   * @return
   */
  protected boolean isUserVisibleHint() {
    return false;
  }

  private void loadData() {
    if (mIsData) {
      if (mIsUserVisibleHint && !mIsVisibleToUser) return;
      initLoad();
    }
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (!mIsVisibleToUser && isVisibleToUser) {
      mIsVisibleToUser = isVisibleToUser;
      loadData();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    int fragmentLayoutId = getLayoutId();
    if (fragmentLayoutId != 0 && fragmentLayoutId != -1) {
      mRootView = inflater.inflate(fragmentLayoutId, null);
    }
    mIsData = initData();
    mIsUserVisibleHint = isUserVisibleHint();
    if (mIsData) {
      initViews();
      onCreateViewFinish();
      loadData();
    }
    return mRootView;
  }

  protected void onCreateViewFinish() {
  }

  /**
   * 隐藏输入法
   */
  protected void showKeyBord(View view) {
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, 2);
  }

  /**
   * 开关输入法
   */
  protected void hideKeyBord() {
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (!getActivity().isFinishing() && imm != null && getActivity().getCurrentFocus() != null) {
      imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
  }

  protected <T extends View> T findViewById(int id) {
    if (mRootView == null) {
      return null;
    }
    return (T) mRootView.findViewById(id);
  }

  protected void showToast(String msg) {
    if (TextUtils.isEmpty(msg)) {
      return;
    }
    if (getActivity() != null && !getActivity().isFinishing()) {
      ToastUtils.show(getActivity(), msg);
    }
  }
}