package com.dicoding.capstonebre.response

import com.google.gson.annotations.SerializedName

data class VerifyResponse(

	@field:SerializedName("data")
	val data: VerifyData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class VerifyData(

	@field:SerializedName("correct")
	val correct: Boolean? = null,

	@field:SerializedName("fun_fact")
	val funFact: String? = null
)
