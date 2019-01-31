package com.colin.component.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.colin.component.R
import com.colin.component.net.ApiException
import com.colin.ctravel.base.BasePresenter
import com.colin.ctravel.base.BaseView
import retrofit2.HttpException

/**
 *create by colin 2018/9/12
 */
abstract class BaseActivity<P : BasePresenter> : AppCompatActivity(), BaseView {

    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(setContentViewId())
        mPresenter = createPresenter()
        initView()
    }


    open fun beforeSetContentView() {

    }

    @LayoutRes
    abstract fun setContentViewId(): Int

    abstract fun createPresenter(): P?


    override fun showLoading() {
        //TODO not

    }

    override fun dismissLoading() {
        //TODO not
    }

    override fun showTipMessage(msg: String) {
        val snackbar = Snackbar.make(getSnackbarView(), msg, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        snackbar.show()
    }

    open fun getSnackbarView(): View {
        return window.decorView.rootView.findViewById(android.R.id.content)
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun getNetKey(): String {
        return this::javaClass.name
    }

    override fun showNetErrorMsg(throwable: Throwable) {
        //进行错误显示
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

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDettach()
        mPresenter = null
    }
}