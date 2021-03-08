package com.saizad.mvvmexample.models

data class ReqResUser(
	val id: Int,
	val firstName: String,
	val lastName: String,
	val email: String,
	val avatar: String
){
	companion object{
		val USER = ReqResUser(1, "John", "Ford", "john.ford@gmail.com", "https://reqres.in/img/faces/2-image.jpg")
	}


}

