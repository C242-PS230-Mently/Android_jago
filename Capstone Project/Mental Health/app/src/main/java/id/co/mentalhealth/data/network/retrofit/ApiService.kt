package id.co.mentalhealth.data.network.retrofit

import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.PredictionResponse
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.response.PhotoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @FormUrlEncoded
    @POST("/predict")
    fun predict(
        @Field("Q1") q1: Int, @Field("Q2") q2: Int, @Field("Q3") q3: Int, @Field("Q4") q4: Int, @Field("Q5") q5: Int,
        @Field("Q6") q6: Int, @Field("Q7") q7: Int, @Field("Q8") q8: Int, @Field("Q9") q9: Int, @Field("Q10") q10: Int,
        @Field("Q11") q11: Int, @Field("Q12") q12: Int, @Field("Q13") q13: Int, @Field("Q14") q14: Int, @Field("Q15") q15: Int,
        @Field("Q16") q16: Int, @Field("Q17") q17: Int, @Field("Q18") q18: Int, @Field("Q19") q19: Int, @Field("Q20") q20: Int,
        @Field("Q21") q21: Int, @Field("Q22") q22: Int, @Field("Q23") q23: Int, @Field("Q24") q24: Int, @Field("Q25") q25: Int
    ): Call<PredictionResponse>

    @Multipart
    @POST("/user/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): PhotoResponse
}
