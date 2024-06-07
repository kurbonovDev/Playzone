package playzone.tj.ui.main.home.event_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.events.EventImagesRequest
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(private val mainAPI: MainAPI) : ViewModel() {
    private var _data = listOf<String>()
    val eventData: List<String> get() = _data


    suspend fun fetchEventImages(eventId: String) {
        if (_data.isEmpty())
            _data = mainAPI.fetchEventImages(EventImagesRequest(eventId))
    }

}