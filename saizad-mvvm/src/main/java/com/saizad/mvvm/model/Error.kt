package com.saizad.mvvm.model

data class Error(
    val description: String,
    val error: String,
    val errorCode: Int,
    val message: String,
    val status: Int
) {
    var fields: List<FieldError>? = null
}