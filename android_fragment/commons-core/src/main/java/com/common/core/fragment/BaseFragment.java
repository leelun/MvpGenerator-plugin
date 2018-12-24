package com.common.core.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.common.core.manager.AppManager;

/**
 * @author: leellun
 */
public class BaseFragment extends Fragment {
  /**
   * @param clazz
   * @Title: toActivity
   * @Description: TODO 进入Activity
   */
  protected void toActivity(Class<?> clazz) {
    Intent intent = new Intent(getActivity(), clazz);
    getActivity().startActivity(intent);
  }


  /**
   * 退出当前Activity
   *
   * @author LiuLun
   * @Time 2015年11月13日下午3:19:33
   */
  protected void finishActivity() {
    AppManager.getAppManager().finishActivity(getActivity());
  }
}
