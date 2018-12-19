package com.common.core.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.R
import com.common.core.utils.ScreenUtils
import com.common.core.widget.ActionBarContainer

/**
 * @date 2016/10/14 17:38
 */
abstract class SimpleActivity : BaseActivity(){
    private var actionBar:View?=null
    private var layout_content:ViewGroup?=null
    private var layout_title_bar:ViewGroup?=null

    /**
     * 获取布局id
     *
     * @author LiuLun
     * @Time 2015年11月9日下午5:09:16
     */
    protected abstract fun getLayoutId(): Int
    protected open fun isHoldStatusBar(): Boolean{
        return false
    }

    /**
     * initialize data
     */
    protected open fun initData(): Boolean {
        return true
    }

    /**
     * 创建组建成功
     *
     * @param savedInstanceState activity保存状态
     * @author LiuLun
     * @Time 2015年11月9日下午2:49:00
     */
    open fun initViews(savedInstanceState: Bundle?) {}

    /**
     * load data,maybe network load
     */
    open fun initLoad(){}

    open fun initActionBar(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initData()) {
            return
        }
        val layoutId = getLayoutId()
        if (layoutId != 0 && layoutId != -1) {
            if(initActionBar()){
                setContentView(R.layout.activity_actionbar_content)
                layout_title_bar=findViewById(R.id.layout_title_bar)
                layout_content=findViewById(R.id.layout_content)
                setUserActionBarView(actionBar!!)
                LayoutInflater.from(this).inflate(layoutId , layout_content)
            }else{
                setContentView(layoutId)
            }
        }
        initViews(savedInstanceState)
        onCreateViewFinish();
        initLoad()
    }

    protected open fun onCreateViewFinish(){};
    /**
     * 设置占位
     *
     * @param actionBarView
     */
    fun setCustomActionBarView(actionBarView: ActionBarContainer) {
        if (isHoldStatusBar()) {
            actionBarView.setPaddingTop(ScreenUtils.getStatusHeight(activity))
        }
        setUserActionBarView(actionBarView)
    }

    /**
     * 设置actionBar
     *
     * @param view
     */
    fun setUserActionBarView(view: View) {
        actionBar=view
        if(layout_title_bar==null)return
        val layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout_title_bar!!.addView(actionBar,layoutParams)
    }

    /**
     * 初始化actionBar
     *
     * @return
     */
    fun showActionBar(resId: Int, title: String, onClickListener: View.OnClickListener): Boolean {
        val container = ActionBarContainer(this)
        container.setLeftImage(resId, onClickListener)
        container.setTitle(title)
        setCustomActionBarView(container)
        return true
    }

}