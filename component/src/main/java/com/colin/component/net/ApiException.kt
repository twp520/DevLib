package com.colin.component.net

/**
 * Created by tianweiping on 2018/1/30.
 */

class ApiException(var code: Int, var msg: String?) : RuntimeException()
