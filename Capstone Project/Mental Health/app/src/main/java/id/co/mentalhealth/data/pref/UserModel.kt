package id.co.mentalhealth.data.pref

data class UserModel(
    val email: String,
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)