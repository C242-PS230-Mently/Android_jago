package id.co.mentalhealth.data.network.response

import com.google.gson.annotations.SerializedName

data class WorkshopResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<WorkshopItem>

)

data class WorkshopItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("image_url")
    val image_url: String,

    @field:SerializedName("full_article_link")
    val full_article_link: String,

)