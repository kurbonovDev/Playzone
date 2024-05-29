package playzone.tj.ui.main.home.event_detail

import androidx.lifecycle.ViewModel
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventImagesRequest
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.utils.mainApi

class EventDetailViewModel : ViewModel() {
    private var _data = listOf<String>()
    val eventData: List<String> get() = _data


    suspend fun fetchEventImages(eventId: String) {
        if (_data.isEmpty())
            _data = mainApi.fetchEventImages(EventImagesRequest(eventId))
    }

}