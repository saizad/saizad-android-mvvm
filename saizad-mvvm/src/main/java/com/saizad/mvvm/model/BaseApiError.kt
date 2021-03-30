package com.saizad.mvvm.model

abstract class BaseApiError {
    abstract fun error(): String
    abstract fun message(): String
}