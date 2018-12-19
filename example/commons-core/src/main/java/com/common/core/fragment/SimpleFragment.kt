package com.common.core.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.common.core.utils.ToastUtils

/**
 * @version V3.0.0
 * @Description: SimpleFragment is default fragment
 * @date 2016/9/13 15:46
 */
abstract class SimpleFragment : BaseFragment() {
    private var mRootView: View? = null
    private var mIsVisibleToUser: Boolean = false
    private var mIsUserVisibleHint: Boolean = false
    private var mIsData = false
    /**
     * 是否已经加载数据
     *
     * @return
     */
    var isAlreadyData = false
        private set

    /**
     * 获取fragment布局
     *
     * @return fragment布局id
     * @author LiuLun
     * @Time 2015年11月10日上午10:26:34
     */
    abstract val fragmentLayoutId: Int

    /**
     * 是否使用uservisiblehint
     *
     * @return
     */
    protected val isUserVisibleHint: Boolean
        get() = false

    /**
     * @return whether continue find views
     * @Description initialize data
     */
    protected fun initData(): Boolean {
        return true
    }

    /**
     * use with [.getFragmentLayoutId]
     */
    protected fun initViews() {}

    /**
     * load data
     */
    protected abstract fun initLoad()

    private fun loadData() {
        if (mIsData) {
            if (mIsUserVisibleHint && !mIsVisibleToUser) return
            initLoad()
            isAlreadyData = true
        }
    }

    fun setVisibleValidate(isValidate: Boolean) {
        mIsVisibleToUser = isValidate
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!mIsVisibleToUser && isVisibleToUser) {
            mIsVisibleToUser = isVisibleToUser
            loadData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentLayoutId = fragmentLayoutId
        if (fragmentLayoutId != 0 && fragmentLayoutId != -1) {
            mRootView = inflater.inflate(fragmentLayoutId, null)
        }
        mIsData = initData()
        mIsUserVisibleHint = isUserVisibleHint
        if (mIsData) {
            initViews()
            loadData()
        }
        return mRootView
    }

    protected fun <T : View> findViewById(id: Int): T? {
        return if (mRootView == null) {
            null
        } else mRootView!!.findViewById<View>(id) as T
    }

    protected fun showToast(msg: String) {
        ToastUtils.show( msg)
    }

    /**
     * 拨打电话
     *
     * @param telphone
     */
    fun startPhoneActivity(telphone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telphone))
        startActivity(intent)
    }
}