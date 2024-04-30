package playzone.tj.retrofit

import playzone.tj.retrofit.models.TokenResponse
import playzone.tj.retrofit.models.forget_password.ForgetPasswordRemote
import playzone.tj.retrofit.models.login.LoginReceiveRemote
import playzone.tj.retrofit.models.registration.Otp
import playzone.tj.retrofit.models.registration.RegisterReceiveRemote
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainAPI {
    @GET("/")
    suspend fun getHello()


    @POST("login")
    suspend fun login(@Body loginReceiveRemote: LoginReceiveRemote):TokenResponse?



    @POST("register")
    suspend fun registerNewUser(@Body registerReceiveRemote: RegisterReceiveRemote)

    @POST("send-otp")
    suspend fun checkOtpCodeForRegister(@Body otp: Otp):TokenResponse?

    @POST("forgetPassword")
    suspend fun sendCheckCodeToEmail(@Body forgetPasswordResponseRemote: ForgetPasswordRemote)

    @POST("confirmPassword")
    suspend fun confirmNewPassword(@Body forgetPasswordResponseRemote: ForgetPasswordRemote)

}