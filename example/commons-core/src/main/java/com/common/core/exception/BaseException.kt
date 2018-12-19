package com.common.core.exception

class BaseException : RuntimeException {

    constructor() : super() {
        // TODO Auto-generated constructor stub
    }

    constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable) {
        // TODO Auto-generated constructor stub
    }

    constructor(detailMessage: String) : super(detailMessage) {
        // TODO Auto-generated constructor stub
    }

    constructor(throwable: Throwable) : super(throwable) {
        // TODO Auto-generated constructor stub
    }

    companion object {

        /**
         * @fieldName: serialVersionUID
         * @fieldType: long
         * @Description: TODO
         */
        private val serialVersionUID = -456218279911987911L
    }

}
