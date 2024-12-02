package id.co.mentalhealth.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<HistoryItem>,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class HistoryItem(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("predictions")
	val predictions: Predictions,

	@field:SerializedName("total_consult")
	val totalConsult: String

): Parcelable