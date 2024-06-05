package playzone.tj.ui.main.home.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import playzone.tj.retrofit.models.GetUser
import playzone.tj.retrofit.models.User
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.user_genres.UserGenresReceive
import playzone.tj.retrofit.models.user_genres.UserGenresResponse
import playzone.tj.utils.LOGIN_KEY
import playzone.tj.utils.STORAGE_KEY
import playzone.tj.utils.TOKEN_KEY
import playzone.tj.utils.mainApi

class HomeViewModel(context: Context) : ViewModel() {
    private var sharedPreferences: SharedPreferences
    private var token: String? = null
    private var login: String? = null

    /*private val _eventUIState: MutableLiveData<EventUIState> =
        MutableLiveData(EventUIState())
    val eventData: LiveData<EventUIState> get() = _eventUIState*/

    private val _eventUIState = MutableStateFlow<EventUIState>(EventUIState.Empty)
    val eventUIState: StateFlow<EventUIState> get() = _eventUIState


    private var _genreUIState: MutableLiveData<UserGenreUIState> =
        MutableLiveData(UserGenreUIState())
    val genreUIState: LiveData<UserGenreUIState> get() = _genreUIState

    private val _userUIState: MutableLiveData<UserUIState> =
        MutableLiveData(UserUIState())
    val userUIState: LiveData<UserUIState> get() = _userUIState

    init {
        Log.d("MyTag", "ViewModel Init")
        sharedPreferences = context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(TOKEN_KEY, "")
        login = sharedPreferences.getString(LOGIN_KEY, "")
        fetchEvents()
        fetchUser()
        fetchUserGenres()
    }

   /*fun fetchEvents() {
        _eventUIState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            if (_eventUIState.value.eventData.isEmpty() && token != null) {
                try {
                    val response = mainApi.fetchEvent(headerValue = token!!, EventRequest(""))
                    if (response.isSuccessful) {
                        _eventUIState.update {
                            it.copy(
                                eventData = response.body() ?: emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    } else {
                        _eventUIState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.message()
                            )
                        }
                    }
                } catch (e: Exception) {
                    _eventUIState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
            }
        }
    }*/

    fun fetchEvents() {
        // Сначала проверяем token, чтобы не запускать корутину зря
        if (token == null) {
            _eventUIState.value = EventUIState.Error("Token is null")
            return
        }

        // Устанавливаем состояние загрузки
        _eventUIState.value = EventUIState.Loading

        viewModelScope.launch {
            // Проверяем состояние _eventUIState внутри корутины
            if (_eventUIState.value is EventUIState.Loading) {
                try {
                    val response = mainApi.fetchEvent(headerValue = token+"54"!!, EventRequest(""))
                    if (response.isSuccessful) {
                        _eventUIState.value = EventUIState.Success(response.body() ?: emptyList())
                    } else {
                        _eventUIState.value = EventUIState.Error(response.message())
                        Log.d("MyTag", "Error response: ${response.message()}")
                    }
                } catch (e: Exception) {
                    _eventUIState.value = EventUIState.Error(e.message ?: "Unknown error")
                    Log.d("MyTag", "Exception: ${e.message}", e)
                }
            }
        }
    }

    /* fun fetchEvents() {
         _eventUIState.value = _eventUIState.value?.copy(
             isLoading = true,
             errorMessage = null
         )
         viewModelScope.launch {
             Log.d("MyTag", "ViewModel fetchEvents1 ${_eventUIState.value}")
             if (_eventUIState.value?.eventData!!.isEmpty() && token != null) {
                 try {
                     val response = mainApi.fetchEvent(headerValue = token!!, EventRequest(""))

                     if (response.isSuccessful){
                         _eventUIState.value = _eventUIState.value?.copy(
                             eventData = response.body()?: emptyList(),
                             isLoading = false,
                             errorMessage = null
                         )
                     }else{
                         _eventUIState.value = _eventUIState.value?.copy(
                             isLoading = false,
                             errorMessage = response.message()
                         )
                         Log.d("MyTag", "ViewModel Response Message: ${ response.message()}")
                     }


                 }catch (e:Exception){
                     _eventUIState.value = _eventUIState.value?.copy(
                         isLoading = false,
                         errorMessage = e.message
                     )
                 }
             }
             Log.d("MyTag", "ViewModel fetchEvents2 ${_eventUIState.value}")
         }

     }*/

    fun fetchUser() {
        _userUIState.value = _userUIState.value?.copy(
            isLoading = true,
            errorMessage = null
        )
        if (_userUIState.value?.user == null) {
            viewModelScope.launch {
                try {
                    val response = mainApi.fetchUser(GetUser(login!!))
                    if (response.isSuccessful) {
                        _userUIState.value = _userUIState.value?.copy(
                            isLoading = false,
                            user = response.body(),
                            errorMessage = null
                        )
                    } else {
                        _userUIState.value = _userUIState.value?.copy(
                            isLoading = false,
                            errorMessage = response.message()
                        )
                    }
                } catch (e: Exception) {
                    _userUIState.value = _userUIState.value?.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun fetchUserGenres() {
        _genreUIState.value = _genreUIState.value?.copy(
            isLoading = true,
            errorMessage = null
        )
        if (_genreUIState.value?.userGenresResponse?.userGenres!!.isEmpty()) {
            viewModelScope.launch {
                try {
                    val response = mainApi.fetchUserGenres(UserGenresReceive(login!!))
                    if (response.isSuccessful) {
                        _genreUIState.value = _genreUIState.value!!.copy(
                            isLoading = false,
                            errorMessage = null,
                            userGenresResponse = response.body() ?: UserGenresResponse(emptyList())
                        )
                    } else {
                        _genreUIState.value = _genreUIState.value!!.copy(
                            isLoading = false,
                            errorMessage = response.message(),
                        )
                    }
                } catch (e: Exception) {
                    _genreUIState.value = _genreUIState.value!!.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }

            }
        }
    }


    fun clearData() {
        /*_eventUIState.value?.eventData = emptyList()
        _eventUIState.value?.errorMessage = null
        _eventUIState.value?.isLoading = false*/
        /*_eventUIState.update {
            it.copy(eventData = emptyList(), errorMessage = null, isLoading = false)
        }*/
        _eventUIState.value = EventUIState.Empty

        _genreUIState.value?.isLoading = false
        _genreUIState.value?.errorMessage = null
        _genreUIState.value?.userGenresResponse?.userGenres = emptyList()


        _userUIState.value?.user = null
        _userUIState.value?.isLoading = false
        _userUIState.value?.errorMessage = null
        Log.d("MyTag", "ViewModel Clear fetchEvents ${_eventUIState.value}")

    }

    class MainViewModelFactory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(context) as T
            }
            throw IllegalAccessException("Unknown ViewModelClass")
        }
    }


}

/*data class EventUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var eventData: List<EventDTO> = emptyList()
)*/

data class UserUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var user: User? = null
)

data class UserGenreUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var userGenresResponse: UserGenresResponse = UserGenresResponse(emptyList())
)

sealed class EventUIState {
    data object Loading : EventUIState()
    data class Success(val eventData: List<EventDTO>) : EventUIState()
    data class Error(val errorMessage: String) : EventUIState()
    data object Empty : EventUIState() // Для случая, когда нет данных
}


