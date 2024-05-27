package playzone.tj.retrofit.models.games

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class GameRequest(
    val searchQuery: String
)

data class GameResponse(
    val result: List<GameDTO>
)
@Parcelize
data class GameDTO(
    val gameID: String,
    val gameName: String,
    val description: String,
    val versionGame: String,
    val sizeGame: String,
    var image:String?,
    var logo:String?,
    val downloadCount:Int,
    val rateGame:Int
):Parcelable

data class GetGameGenre(
    val gameId:String
)