package id.co.mentalhealth.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class WorkshopResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<DashboardItem>

)

@Parcelize
data class DashboardItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("image_url")
    val image_url: String,

    @field:SerializedName("full_article_link")
    val full_article_link: String,

): Parcelable

data class ArticleResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<DashboardItem>
)

data class DoctorsResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("doctors")
    val doctor: List<DoctorsItem>
)

@Parcelize
data class DoctorsItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("specialization")
    val specialization: String,

    @field:SerializedName("image_url")
    val image_url: String,

    @field:SerializedName("location")
    val location: String

): Parcelable