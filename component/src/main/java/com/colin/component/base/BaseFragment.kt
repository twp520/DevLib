package com.colin.component.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.colin.component.R
import com.colin.component.net.ApiException
import com.colin.ctravel.base.BaseView
import retrofit2.HttpException

/**
 *create by colin 2018/9/26
 */
abstract class BaseFragment : Fragment(), BaseView {


    private var isPrepared: Boolean? = false
    private var isFirst = false

    override fun initView() {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showTipMessage(msg: String) {
        val snackbar = Snackbar.make(getSnackbarView(), msg, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        snackbar.show()
    }

    abstract fun getSnackbarView(): View

    override fun getViewContext(): Context {
        return activity!!
    }

    override fun getNetKey(): String {
        return this::javaClass.name
    }

    override fun showNetErrorMsg(throwable: Throwable) {
        dismissLoading()
        when (throwable) {
            is ApiException -> showTipMessage(throwable.msg ?: "")
            is HttpException -> showTipMessage("http:" + throwable.message)
            else -> {
                showTipMessage("some thing:" + throwable.message)
                throwable.printStackTrace()
            }
        }
        throwable.printStackTrace()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = createPresenter()
        if (mPresenter != null)
            mPresenter.onAttach(context)
        initView()
    }

    //在这里取出数据
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isFirst = true
        initPrepare()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirst) {
                initPrepare()
            } else if (isPrepared) {
                onUserVisible()
            }
        } else {
            onUserInVisible()
        }
    }

    @Synchronized
    private fun initPrepare() {
        if (isPrepared) {
            onFirstUserVisible()
            isFirst = false
        } else {
            isPrepared = true
        }
    }

    fun onFirstUserVisible() {

    }

    fun onUserVisible() {}

    fun onUserInVisible() {}
}