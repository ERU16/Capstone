package com.dicoding.capstonebre.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: UserRegistration? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UserRegistration(

	@field:SerializedName("profilePicture")
	val profilePicture: String? = null,

	@field:SerializedName("insertedAt")
	val insertedAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
