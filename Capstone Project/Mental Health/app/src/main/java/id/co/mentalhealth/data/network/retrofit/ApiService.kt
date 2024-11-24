package id.co.mentalhealth.data.network.retrofit

import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

//    Authentication
    @FormUrlEncoded
    @POST("/auth/login")
    fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/auth/register")
    fun register(
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("age") age: String,
        @Field("gender") gender: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<RegisterResponse>
}
