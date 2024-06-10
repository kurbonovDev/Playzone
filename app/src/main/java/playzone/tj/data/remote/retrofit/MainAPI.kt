package playzone.tj.data.remote.retrofit

import androidx.lifecycle.MutableLiveData
import playzone.tj.data.remote.retrofit.models.GetUser
import playzone.tj.data.remote.retrofit.models.TokenResponse
import playzone.tj.data.remote.retrofit.models.User
import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.data.remote.retrofit.models.events.EventImagesRequest
import playzone.tj.data.remote.retrofit.models.events.EventRequest
import playzone.tj.data.remote.retrofit.models.events.EventResponse
import playzone.tj.data.remote.retrofit.models.forget_password.ForgetPasswordRemote
import playzone.tj.data.remote.retrofit.models.games.GameDTO
import playzone.tj.data.remote.retrofit.models.games.GameRequest
import playzone.tj.data.remote.retrofit.models.games.GameResponse
import playzone.tj.data.remote.retrofit.models.games.GetGameGenre
import playzone.tj.data.remote.retrofit.models.login.LoginReceiveRemote
import playzone.tj.data.remote.retrofit.models.registration.Otp
import playzone.tj.data.remote.retrofit.models.registration.RegisterReceiveRemote
import playzone.tj.data.remote.retrofit.models.user_genres.UserGenresReceive
import playzone.tj.data.remote.retrofit.models.user_genres.UserGenresResponse
import playzone.tj.data.remote.retrofit.models.user_genres.UsersGenresRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MainAPI {

    @POST("login")
    suspend fun login(@Body loginReceiveRemote: LoginReceiveRemote): TokenResponse?

    @POST("register")
    suspend fun registerNewUser(@Body registerReceiveRemote: RegisterReceiveRemote)

    @POST("send-otp")
    suspend fun checkOtpCodeForRegister(@Body otp: Otp): TokenResponse?

    @POST("forgetPassword")
    suspend fun sendCheckCodeToEmail(@Body forgetPasswordResponseRemote: ForgetPasswordRemote)

    @POST("confirmPassword")
    suspend fun confirmNewPassword(@Body forgetPasswordResponseRemote: ForgetPasswordRemote)

    @POST("games/fetch")
    suspend fun fetchGame(@Body gameRequest: GameRequest): GameResponse

    @POST("events/fetch")
    suspend fun fetchEvent(@Header("Bearer-Authorization") headerValue: String,
                           @Body eventRequest: EventRequest
    ):Response<List<EventDTO>>


    @POST("add/genres_to_user")
    suspend fun addGenresToUser(@Body usersGenresRequest: UsersGenresRequest)

    @POST("fetch/user_genres")
    suspend fun fetchUserGenres(@Body userGenresReceive: UserGenresReceive):Response<UserGenresResponse>

    @POST("fetch/user")
    suspend fun fetchUser(@Body getUser: GetUser): Response<User>

    @POST("update_user_info")
    suspend fun updateUser(@Body user: User)


    @POST("games/fetch")
    suspend fun fetchGames(@Header("Bearer-Authorization") headerValue: String,
                           @Body gameRequest: GameRequest
    ):List<GameDTO>


    @POST("games/genres")
    suspend fun fetchGameGenre(@Body getGameGenre: GetGameGenre):List<String>

    @POST("events/fetch/images")
    suspend fun fetchEventImages(@Body eventImagesRequest: EventImagesRequest):List<String>

    @POST("games/fetch")
    suspend fun findGames(@Header("Bearer-Authorization") headerValue: String,
                           @Body gameRequest: GameRequest
    ):List<GameDTO>



}