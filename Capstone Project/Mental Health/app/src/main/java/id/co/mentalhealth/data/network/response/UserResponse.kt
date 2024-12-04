package id.co.mentalhealth.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuestionResponse(

	@field:SerializedName("questions")
	val questions: List<QuestionsItem>
)


data class QuestionsItem(

	@field:SerializedName("question_text")
	val questionText: String,

	@field:SerializedName("disorders_id")
	val disordersId: Int,

	@field:SerializedName("question_id")
	val questionId: Int
)

//history item
@Parcelize
data class PredictionResponse(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("predictions")
	val predictions: Predictions,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("created_at")
	val createdAt: String
): Parcelable

@Parcelize
data class Predictions(

	@field:SerializedName("Level Bipolar")
	val levelBipolar: Int,

	@field:SerializedName("Level OCD")
	val levelOCD: Int,

	@field:SerializedName("Level Kecemasan")
	val levelKecemasan: Int,

	@field:SerializedName("Level Depresi")
	val levelDepresi: Int,

	@field:SerializedName("Level Skizofrenia")
	val levelSkizofrenia: Int,

	@field:SerializedName("Nama Solusi")
	val namaSolusi: String,

	@field:SerializedName("Solusi")
	val solusi: String

): Parcelable
