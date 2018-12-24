package com.common.core.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.common.core.exception.FileExceptionHandler;
import com.common.core.utils.ScreenUtils;


/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/10 18:24
 */
public abstract class BaseApplication extends MultiDexApplication {

  private static Application mApplication;


  private int mScreenHeightDp;
  private int mScreenWidthDp;

  public static Application getInstance() {
    return mApplication;
  }

  public void onCreate() {
    super.onCreate();
    mApplication = this;
    if (getDefaultUncaughtExceptionHandler() == null) {
      Thread.setDefaultUncaughtExceptionHandler(new FileExceptionHandler(mApplication));
    } else {
      Thread.setDefaultUncaughtExceptionHandler(getDefaultUncaughtExceptionHandler());
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
      StrictMode.setVmPolicy(builder.build());
    }
    mScreenHeightDp = ScreenUtils.getScreenHeight(this);
    mScreenWidthDp = ScreenUtils.getScreenWidth(this);
  }

  /**
   * 返回默认未捕获异常处理器
   *
   * @return
   * @throws
   * @author LiuLun
   * @Time 2015年11月9日下午4:43:49
   */
  public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
    return null;
  }

  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = res.getConfiguration();
    config.setToDefaults();
    config.screenHeightDp = mScreenHeightDp;
    config.screenWidthDp = mScreenWidthDp;
    res.updateConfiguration(config, super.getResources().getDisplayMetrics());
    return res;
  }
}