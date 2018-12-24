package com.common.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.common.core.manager.AppManager;
import com.common.core.R;

/**
 * @author: leellun
 * @data: 2018/12/19.
 */
public class BaseActivity extends AppCompatActivity {
  /**
   * @return 当前activity
   * @Time 2015年12月7日下午5:56:27
   */
  public Activity getActivity(){
    return this;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppManager.getAppManager().addActivity(this);
  }

  public void onDestroy() {
    AppManager.getAppManager().removeActivity(this);
    super.onDestroy();
  }

  /**
   * 退出当前Activity
   *
   * @author LiuLun
   * @Time 2015年11月13日下午3:19:33
   */
  protected void finishActivity() {
    AppManager.getAppManager().finishActivity(this);
  }

  protected void finishActivity(int resultCode, Intent intent) {
    setResult(resultCode, intent);
    finishActivity();
  }

  /**
   * @param clazz
   * @Title: toActivity
   * @Description: TODO 进入Activity
   */
  protected void toActivity(Class<?> clazz) {
    toActivity(clazz, false, false);
  }

  /**
   * 带动画的跳转
   *
   * @param clazz
   */
  protected void toActivityWithAnim(Class<?> clazz) {
    toActivity(clazz, false, true);
  }

  /**
   * @param clazz
   * @param isClose 是否关闭当前activity
   * @Title: toActivity
   * @Description: TODO 进入Activity
   */
  protected void toActivity(Class<?> clazz, boolean isClose, boolean isAnim) {
    toActivity(clazz, isClose, isAnim ? AnimationDirection.RIGHT_TO_LEFT : AnimationDirection.DEFAULT);
  }

  /**
   * 动画方向
   *
   * @param clazz
   * @param isClose
   * @param direction
   */
  protected void toActivity(Class<?> clazz, boolean isClose, AnimationDirection direction) {
    if (clazz == null)
      return;
    Intent intent = new Intent(this, clazz);
    startActivity(intent);
    switch (direction) {
      case LEFT_TO_RIGHT:
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
        break;
      case RIGHT_TO_LEFT:
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
        break;
    }
    if (isClose)
      finishActivity();
  }

  /**
   * 动画方向
   *
   * @param clazz
   * @param requestCode
   */
  protected void toActivityForResultWithAnim(Class<?> clazz, int requestCode) {
    toActivityForResult(clazz, requestCode, AnimationDirection.RIGHT_TO_LEFT);
  }

  /**
   * 动画方向
   *
   * @param clazz
   * @param requestCode
   * @param direction
   */
  protected void toActivityForResult(Class<?> clazz, int requestCode, AnimationDirection direction) {
    if (clazz == null)
      return;
    Intent intent = new Intent(this, clazz);
    startActivityForResult(intent, requestCode);
    switch (direction) {
      case LEFT_TO_RIGHT:
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
        break;
      case RIGHT_TO_LEFT:
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
        break;
    }
  }

  /**
   * 带动画的activity启动
   */
  public void startActivityWithAnim(Intent intent, boolean isClose) {
    startActivity(intent);
    overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
    if (isClose) {
      finishActivity();
    }
  }

  /**
   * 带动画的activity启动
   */
  public void startActivityForResultWithAnim(Intent intent, int requestCode) {
    startActivityForResult(intent, requestCode);
    overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
  }

  /**
   * 带动画的finish
   */
  protected void finishWithAnim() {
    finishActivity();
    overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
  }

  protected void finishActivity(AnimationDirection direction) {
    finishActivity();
    switch (direction) {
      case LEFT_TO_RIGHT:
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
        break;
      case RIGHT_TO_LEFT:
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
        break;
    }
  }

  public enum AnimationDirection {
    DEFAULT, LEFT_TO_RIGHT, RIGHT_TO_LEFT
  }

}
