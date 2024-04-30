package playzone.tj.retrofit.models.forget_password


data class ForgetPasswordRemote(
    val email:String,
    val checkCode:String?,
    val newPassword: String?
)