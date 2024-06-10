package playzone.tj.data.remote.retrofit.models.user_genres

import androidx.annotation.DrawableRes

data class UsersGenresRequest(
    val userLogin: String,
    val genreId: List<String>
)

data class UserGenresReceive(
    val login:String,
)

data class UserGenresResponse(
    var userGenres:List<String>
)

data class Genres(
    @DrawableRes
    val image:Int,
    val genreName:String
)