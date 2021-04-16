package com.avinash.kotlinmvvmandcleanarchitecture.util

class APIError {
    private var statusCode = 0
    private var code = 0


    private var message: String? = null


    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun getStatusCode(): Int {
        return statusCode
    }

    fun setStatusCode(statusCode: Int) {
        this.statusCode = statusCode
    }
}