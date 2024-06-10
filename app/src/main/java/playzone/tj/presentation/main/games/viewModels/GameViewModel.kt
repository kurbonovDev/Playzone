package playzone.tj.presentation.main.games.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import playzone.tj.data.remote.retrofit.MainAPI
import playzone.tj.data.remote.retrofit.models.games.GameDTO
import playzone.tj.data.remote.retrofit.models.games.GameRequest
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val mainAPI: MainAPI) : ViewModel() {

    private var _data = listOf<GameDTO>()
    val gameData: List<GameDTO> get() = _data


    private var _findData = emptyList<GameDTO>()
    val findData: List<GameDTO> get() = _findData

    suspend fun fetchGames(token: String) {
        if (_data.isEmpty())
            _data = mainAPI.fetchGames(token, GameRequest(""))
    }

    suspend fun findGames(token: String, searchQuery: String) {
        _findData = mainAPI.findGames(
            headerValue = token,
            gameRequest = GameRequest(searchQuery)
        )
        Log.d("MyTag", "suspend findGames:$findData")
    }

}