package playzone.tj.data.remote.retrofit.models.events

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import playzone.tj.domain.models.DomainEventDTO

data class EventRequest(
    val searchQuery: String
)

data class EventResponse(
    val result: List<EventDTO>
)
@Parcelize
data class EventDTO(
    val eventId:String,
    var gameId:String,
    val eventName:String,
    val title:String,
    val creatorName:String,
    var imagePreview:String,
    var imageMain:String,
    val imageCreator:String,
    val description:String,
    val status:String,
    val format:String,
    var watcherCount:Int,
    val link:String
):Parcelable


data class EventImagesRequest(
    val eventId: String
)