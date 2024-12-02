package id.co.mentalhealth.data.network.retrofit

import id.co.mentalhealth.data.network.response.ArticleResponse
import id.co.mentalhealth.data.network.response.DoctorsResponse
import id.co.mentalhealth.data.network.response.HistoryResponse
import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.PhotoResponse
import id.co.mentalhealth.data.network.response.PredictionResponse
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.response.WorkshopResponse
import okhttp3.MultipartBody
import retrofit2.Response
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
//    Question
    @GET("/user/questions")
    suspend fun getQuestions(): QuestionResponse

    @FormUrlEncoded
    @POST("/user/predict")
    suspend fun predict(
        @Field("Q1") q1: Int, @Field("Q2") q2: Int, @Field("Q3") q3: Int, @Field("Q4") q4: Int, @Field("Q5") q5: Int,
        @Field("Q6") q6: Int, @Field("Q7") q7: Int, @Field("Q8") q8: Int, @Field("Q9") q9: Int, @Field("Q10") q10: Int,
        @Field("Q11") q11: Int, @Field("Q12") q12: Int, @Field("Q13") q13: Int, @Field("Q14") q14: Int, @Field("Q15") q15: Int,
        @Field("Q16") q16: Int, @Field("Q17") q17: Int, @Field("Q18") q18: Int, @Field("Q19") q19: Int, @Field("Q20") q20: Int,
        @Field("Q21") q21: Int, @Field("Q22") q22: Int, @Field("Q23") q23: Int, @Field("Q24") q24: Int, @Field("Q25") q25: Int
    ): Response<PredictionResponse>

    @GET("/user/history")
    suspend fun getHistory(): HistoryResponse

//    Profile
    @Multipart
    @POST("/user/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): PhotoResponse


//    dashboard
    @GET("/user/dashboard/workshop")
    suspend fun getWorkshop(): WorkshopResponse

    @GET("/user/dashboard/article")
    suspend fun getArticle(): ArticleResponse

    @GET("/user/doctors")
    suspend fun getDoctors(): DoctorsResponse
}
