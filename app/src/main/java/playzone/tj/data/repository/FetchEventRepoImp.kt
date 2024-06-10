package playzone.tj.data.repository

import playzone.tj.data.remote.retrofit.MainAPI
import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.data.remote.retrofit.models.events.EventRequest
import playzone.tj.domain.repository.FetchEventRepository
import retrofit2.Response
import javax.inject.Inject

class FetchEventRepoImp @Inject constructor(
    private val mainAPI: MainAPI
) : FetchEventRepository {
    override suspend fun fetchEvents(token: String): Response<List<EventDTO>> {
        return mainAPI.fetchEvent(headerValue = token, EventRequest(""))
    }
}