package com.common.core.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.common.core.utils.ScreenUtils;
import com.common.core.widget.ActionBarContainer;
import com.common.core.R;

/**
 * @date 2016/10/14 17:38
 */
public abstract class SimpleActivity extends BaseActivity{
    private View actionBar;
    private ViewGroup layout_content;
    private ViewGroup layout_title_bar;

    /**
     * 获取布局id
     *
     * @author LiuLun
     * @Time 2015年11月9日下午5:09:16
     */
    protected abstract int getLayoutId();
    protected boolean isHoldStatusBar(){
        return false;
    }

    /**
     * initialize data
     */
    protected boolean  initData() {
        return true;
    }

    /**
     * 创建组建成功
     *
     * @param savedInstanceState activity保存状态
     * @author LiuLun
     * @Time 2015年11月9日下午2:49:00
     */
    public void initViews(Bundle savedInstanceState) {}

    /**
     * load data,maybe network load
     */
    public void initLoad(){}

  public boolean  initActionBar() {
        return false;
    }

  public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!initData()) {
            return;
        }
        int layoutId = getLayoutId();
        if (layoutId != 0 && layoutId != -1) {
            if(initActionBar()){
                setContentView(R.layout.activity_actionbar_content);
                layout_title_bar=findViewById(R.id.layout_title_bar);
                layout_content=findViewById(R.id.layout_content);
                setUserActionBarView(actionBar);
                LayoutInflater.from(this).inflate(layoutId , layout_content);
            }else{
                setContentView(layoutId);
            }
        }
        initViews(savedInstanceState);
        onCreateViewFinish();
        initLoad();
    }

    protected void onCreateViewFinish(){};
    /**
     * 设置占位
     *
     * @param actionBarView
     */
    public void  setCustomActionBarView(ActionBarContainer actionBarView) {
        if (isHoldStatusBar()) {
            actionBarView.setPaddingTop(ScreenUtils.getStatusHeight(getActivity()));
        }
        setUserActionBarView(actionBarView);
    }

    /**
     * 设置actionBar
     *
     * @param view
     */
    public void  setUserActionBarView(View view) {
        actionBar=view;
        if(layout_title_bar==null)return;
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_title_bar.addView(actionBar,layoutParams);
    }

    /**
     * 初始化actionBar
     *
     * @return
     */
    public boolean  showActionBar(int resId,String  title,View.OnClickListener onClickListener) {
        ActionBarContainer container = new ActionBarContainer(this);
        container.setLeftImage(resId, onClickListener);
        container.setTitle(title);
        setCustomActionBarView(container);
        return true;
    }

}