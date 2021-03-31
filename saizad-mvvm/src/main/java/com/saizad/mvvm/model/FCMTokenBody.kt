package com.saizad.mvvm.model

class FCMTokenBody @JvmOverloads constructor(
    name: String,
    private val deviceId: String,
    private val registrationId: String,
    private val active: Boolean = true
) : NameModel(name = name) {
    private val type = "android"
}