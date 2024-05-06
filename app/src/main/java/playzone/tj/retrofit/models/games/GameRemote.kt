package playzone.tj.retrofit.models.games


data class GameRequest(
    val searchQuery: String
)

data class GameResponse(
    val result: List<GameDTO>
)

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
)