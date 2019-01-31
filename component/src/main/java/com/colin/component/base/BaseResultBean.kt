package com.colin.component.base

/**
 * Created by tianweiping on 2017/11/1.
 */

class BaseResultBean<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null
}
