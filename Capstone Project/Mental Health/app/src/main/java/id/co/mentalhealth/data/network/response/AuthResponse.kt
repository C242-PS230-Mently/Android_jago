package id.co.mentalhealth.data.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("user")
    val user: User? = null
)

data class LoginResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("user")
    val user: User? = null
)

data class User(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("fullName")
    val fullName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("photo")
    val photo: String? = null,
)

data class PasswordResponse(
    @field:SerializedName("msg")
    val msg: String
)