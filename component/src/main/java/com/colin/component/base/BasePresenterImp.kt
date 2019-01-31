package com.colin.component.base

import android.os.Bundle
import com.colin.component.net.RxNetLife
import com.colin.ctravel.base.BasePresenter
import com.colin.ctravel.base.BaseView

/**
 *create by colin 2018/9/12
 */
open class BasePresenterImp<V : BaseView>(var view: V?) : BasePresenter {


    override fun onAttach(bundle: Bundle) {

    }

    override fun onDettach() {
        RxNetLife.getNetLife().clear(view?.getNetKey())
        view = null
    }

}