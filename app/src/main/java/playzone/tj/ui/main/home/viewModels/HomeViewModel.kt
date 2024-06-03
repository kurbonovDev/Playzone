package playzone.tj.ui.main.home.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playzone.tj.retrofit.models.GetUser
import playzone.tj.retrofit.models.User
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.user_genres.UserGenresReceive
import playzone.tj.retrofit.models.user_genres.UserGenresResponse
import playzone.tj.utils.mainApi

class HomeViewModel : ViewModel() {

    private var _data = listOf<EventDTO>()
    val eventData: List<EventDTO> get() = _data

    private var _genreData: UserGenresResponse? = null
    val genreData: UserGenresResponse get() = _genreData!!

    private var _userData: User? = null
    val userData: User get() = _userData!!



    suspend fun fetchEvents(token: String) {
        _data = mainApi.fetchEvent(headerValue = token, EventRequest(""))
    }

    suspend fun fetchUserGenres(login: String) {
        if (_genreData == null)
            _genreData = mainApi.fetchUserGenres(UserGenresReceive(login = login))
    }

    suspend fun fetchUser(login: String) {
        Log.d("MyTag","$_userData")
        if (_userData == null)
            _userData = mainApi.fetchUser(GetUser(login))
    }


    fun clearData() {
        _data = emptyList()
        _genreData = null
        _userData = null
    }
}