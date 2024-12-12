package com.dicoding.capstonebre.response

import com.google.gson.annotations.SerializedName

data class QuestionResponse(

	@field:SerializedName("data")
	val data: QuestionData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class QuestionData(

	@field:SerializedName("animal_name")
	val animalName: String? = null,

	@field:SerializedName("quiz_id")
	val quizId: Int? = null,

	@field:SerializedName("quiz_options")
	val quizOptions: List<String?>? = null,

	@field:SerializedName("correct_answer")
	val correctAnswer: String? = null,

	@field:SerializedName("quiz_question")
	val quizQuestion: String? = null,

	@field:SerializedName("fun_fact")
	val funFact: String? = null
)
