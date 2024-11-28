package id.co.mentalhealth.data.network.retrofit

import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

//    Authentication
    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("age") age: String,
        @Field("gender") gender: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("/user/questions")
    suspend fun getQuestions(): QuestionResponse
}
