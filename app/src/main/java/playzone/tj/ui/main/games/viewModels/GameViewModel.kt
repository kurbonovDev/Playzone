package playzone.tj.ui.main.games.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.games.GameDTO
import playzone.tj.retrofit.models.games.GameRequest
import playzone.tj.retrofit.models.games.GameResponse
import playzone.tj.utils.mainApi

class GameViewModel : ViewModel() {

    private var _data = listOf<GameDTO>()
    val gameData: List<GameDTO> get() = _data


    private var _findData = emptyList<GameDTO>()
     val findData: List<GameDTO> get() = _findData

    suspend fun fetchGames(token: String) {
        if (_data.isEmpty())
            _data = mainApi.fetchGames(token, GameRequest(""))
    }



    suspend fun findGames(token: String, searchQuery: String) {
        _findData = mainApi.findGames(
            headerValue = token,
            gameRequest = GameRequest(searchQuery)
        )
        Log.d("MyTag","suspend findGames:$findData")
    }

}