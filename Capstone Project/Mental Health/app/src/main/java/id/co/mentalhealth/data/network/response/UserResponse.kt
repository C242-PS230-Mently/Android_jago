package id.co.mentalhealth.data.network.response

import com.google.gson.annotations.SerializedName

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
