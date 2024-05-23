package playzone.tj.retrofit.models.events

data class EventRequest(
    val searchQuery: String
)

data class EventResponse(
    val result: List<EventDTO>
)

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
)
