package playzone.tj.ui.main.home.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    private val _eventUIState: MutableLiveData<EventUIState> =
        MutableLiveData(EventUIState())
    val eventData: LiveData<EventUIState> get() = _eventUIState

    private var _genreData = MutableLiveData<UserGenresResponse?>()
    val genreData: LiveData<UserGenresResponse?> get() = _genreData

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> get() = _userData

    init {
        Log.d("MyTag", "ViewModel Init")
        sharedPreferences = context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(TOKEN_KEY, "")
        login = sharedPreferences.getString(LOGIN_KEY, "")
        fetchEvents()
        fetchUser()
        fetchUserGenres()
    }


    fun fetchEvents() {
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

    }

    fun fetchUser() {
        viewModelScope.launch {
            if (_userData.value == null && login != null) {
                val user = mainApi.fetchUser(GetUser(login!!))
                _userData.value = user
            }
        }
    }

    fun fetchUserGenres() {
        viewModelScope.launch {
            if (_genreData.value == null && login != null) {
                val userGenres = mainApi.fetchUserGenres(UserGenresReceive(login = login!!))
                _genreData.value = userGenres

            }
        }
    }


    fun clearData() {
        _eventUIState.value?.eventData = emptyList()
        _eventUIState.value?.errorMessage = null
        _eventUIState.value?.isLoading = false


        _genreData.value = null
        _userData.value = null
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
    data class EventUIState(
        var isLoading: Boolean = false,
        var errorMessage: String? = null,
        var eventData:List<EventDTO> = emptyList()
    )
}


