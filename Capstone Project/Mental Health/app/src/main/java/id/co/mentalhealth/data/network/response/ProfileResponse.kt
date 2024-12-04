package id.co.mentalhealth.data.network.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("username")
	val username: String
) : Parcelable

data class PhotoResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("url")
	val url: String? = null

)

