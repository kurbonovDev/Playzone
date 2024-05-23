package playzone.tj.retrofit.models

data class User(
    val login:String,
    val password:String,
    val email:String?,
    val username:String,
    val userImage:String?
)

data class TokenResponse(
    val token:String
)

data class GetUser(
    val login: String
)
