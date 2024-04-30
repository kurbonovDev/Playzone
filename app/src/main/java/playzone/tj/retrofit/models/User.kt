package playzone.tj.retrofit.models

data class User(
    val login:String,
    val password:String,
    val email:String?,
    val username:String
)

data class TokenResponse(
    val token:String
)
