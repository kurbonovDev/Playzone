package playzone.tj.domain.repository

import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.domain.models.DomainEventDTO
import retrofit2.Response

interface FetchEventRepository {
    suspend fun fetchEvents(token: String):Response<List<EventDTO>>
}