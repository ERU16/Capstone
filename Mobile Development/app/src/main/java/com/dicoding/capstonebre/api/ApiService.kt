package com.dicoding.capstonebre.api

import com.dicoding.capstonebre.response.AnimalResponse
import com.dicoding.capstonebre.response.DetailResponse
import com.dicoding.capstonebre.response.EditResponse
import com.dicoding.capstonebre.response.GetUserResponse
import com.dicoding.capstonebre.response.LoginResponse
import com.dicoding.capstonebre.response.QuestionResponse
import com.dicoding.capstonebre.response.RequestQBody
import com.dicoding.capstonebre.response.VerifyResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("/api/animals")
    suspend fun getAnimals(): Response<AnimalResponse>

    @GET("/api/animals/{animalName}")
    suspend fun getAnimalDetail(@Path("animalName") animalName: String): Response<DetailResponse>

    @Multipart
    @POST("/api/auth/register")
    suspend fun registerUser(
        @Part("username") username: RequestBody,
        @Part("age") age: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null
    ): Response<LoginResponse>

    @Multipart
    @PUT("/api/users/{username}")
    suspend fun updateUserProfile(
        @Path("username") username: String,
        @Part("usernameUpdate") usernameUpdate: RequestBody,
        @Part("ageUpdate") ageUpdate: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null
    ): Response<EditResponse>


    @GET("/api/users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): Response<GetUserResponse>

    @GET("/api/quizzes/{animalName}")
    suspend fun getAnimalQuestions(@Path("animalName") name: String): Response<QuestionResponse>

    @POST("/api/quizzes/{animalName}/verify")
    suspend fun verifyAnswer(@Path("animalName") animalName: String, @Body requestQBody: RequestQBody): Response<VerifyResponse>

}

