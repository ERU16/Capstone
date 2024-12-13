package com.dicoding.capstonebre.response

import com.google.gson.annotations.SerializedName

data class RequestQBody(

	@field:SerializedName("answer")
	val answer: String? = null
)
