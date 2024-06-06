package playzone.tj.ui.main.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import playzone.tj.retrofit.models.GetUser
import playzone.tj.retrofit.models.User
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.user_genres.UserGenresReceive
import playzone.tj.retrofit.models.user_genres.UserGenresResponse
import playzone.tj.utils.mainApi

class HomeViewModel() : ViewModel() {

    private val _eventUIState = MutableStateFlow<EventUIState>(EventUIState.Empty)
    val eventUIState: StateFlow<EventUIState> get() = _eventUIState

    private var _genreUIState = MutableStateFlow<UserGenreUIState>(UserGenreUIState.Empty)
    val genreUIState: StateFlow<UserGenreUIState> get() = _genreUIState

    private val _userUIState = MutableStateFlow<UserUIState>(UserUIState.Empty)
    val userUIState: StateFlow<UserUIState> get() = _userUIState


    fun fetchEvents(token: String?) {
        if (token == null) {
            _eventUIState.value = EventUIState.Error("Token is null")
            return
        }
        viewModelScope.launch {
            if (_eventUIState.value is EventUIState.Empty) {
                _eventUIState.value = EventUIState.Loading
                try {
                    val response = mainApi.fetchEvent(headerValue = token, EventRequest(""))
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            _eventUIState.value = EventUIState.Empty
                        }
                        _eventUIState.value = EventUIState.Success(response.body() ?: emptyList())
                    } else {
                        _eventUIState.value = EventUIState.Error(response.message())
                    }
                } catch (e: Exception) {
                    _eventUIState.value = EventUIState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun fetchUser(login: String?) {
        if (login == null) {
            _userUIState.value = UserUIState.Error("Login is null")
            return
        }
        viewModelScope.launch {
            if (_userUIState.value is UserUIState.Empty) {
                _userUIState.value = UserUIState.Loading
                try {
                    val response = mainApi.fetchUser(GetUser(login))
                    if (response.isSuccessful) {
                        _userUIState.value = UserUIState.Success(response.body()!!)
                    } else {
                        _userUIState.value = UserUIState.Error(response.message())
                    }
                } catch (e: Exception) {
                    _userUIState.value = UserUIState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun fetchUserGenres(login: String?) {
        if (login == null) {
            _genreUIState.value = UserGenreUIState.Error("Login is null")
            return
        }

        viewModelScope.launch {
            if (_genreUIState.value is UserGenreUIState.Empty) {
                _genreUIState.value = UserGenreUIState.Loading
                try {
                    val response = mainApi.fetchUserGenres(UserGenresReceive(login))
                    if (response.isSuccessful) {
                        _genreUIState.value = UserGenreUIState.Success(
                            response.body() ?: UserGenresResponse(
                                emptyList()
                            )
                        )
                    } else {
                        _genreUIState.value = UserGenreUIState.Error(response.message())
                    }
                } catch (e: Exception) {
                    _genreUIState.value = UserGenreUIState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun clearData() {
        _eventUIState.value = EventUIState.Empty
        _genreUIState.value = UserGenreUIState.Empty
        _userUIState.value = UserUIState.Empty
    }


}
sealed class EventUIState {
    data object Loading : EventUIState()
    data class Success(val eventData: List<EventDTO>) : EventUIState()
    data class Error(val errorMessage: String) : EventUIState()
    data object Empty : EventUIState()
}

sealed class UserGenreUIState {
    data object Loading : UserGenreUIState()
    data class Success(val userGenreData: UserGenresResponse = UserGenresResponse(emptyList())) :
        UserGenreUIState()

    data class Error(val errorMessage: String) : UserGenreUIState()
    data object Empty : UserGenreUIState()
}

sealed class UserUIState {
    data object Loading : UserUIState()
    data class Success(val userData: User) : UserUIState()
    data class Error(val errorMessage: String) : UserUIState()
    data object Empty : UserUIState()
}
