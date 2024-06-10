package playzone.tj.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class DomainEventDTO(
    val eventId: String,
    var gameId: String,
    val eventName: String,
    val title: String,
    val creatorName: String,
    var imagePreview: String,
    var imageMain: String,
    val imageCreator: String,
    val description: String,
    val status: String,
    val format: String,
    var watcherCount: Int,
    val link: String

) : Parcelable