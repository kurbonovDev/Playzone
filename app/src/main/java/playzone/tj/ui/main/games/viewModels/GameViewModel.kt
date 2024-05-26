package playzone.tj.ui.main.games.viewModels

import androidx.lifecycle.ViewModel
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.games.GameDTO
import playzone.tj.retrofit.models.games.GameRequest
import playzone.tj.utils.mainApi

class GameViewModel : ViewModel() {

    private var _data = listOf<GameDTO>()
    val gameData: List<GameDTO> get() = _data

    suspend fun fetchGames(token: String) {
        if (_data.isEmpty())
            _data = mainApi.fetchGames(token, GameRequest(""))
    }
}