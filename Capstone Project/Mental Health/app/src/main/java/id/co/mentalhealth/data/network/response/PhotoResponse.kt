package id.co.mentalhealth.data.network.response

import com.google.gson.annotations.SerializedName

data class PhotoResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("url")
    val url: String? = null

)
