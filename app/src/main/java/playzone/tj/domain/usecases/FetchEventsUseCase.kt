package playzone.tj.domain.usecases

import playzone.tj.data.mapper.EventItemMapper
import playzone.tj.data.repository.FetchEventRepoImp
import playzone.tj.domain.models.DomainEventState
import javax.inject.Inject

class FetchEventsUseCase @Inject constructor(
    private val eventRepository: FetchEventRepoImp,
    private val eventItemMapper: EventItemMapper
) {
    suspend fun execute(token: String): DomainEventState {
        return try {
            val response = eventRepository.fetchEvents(token)
            if (response.isSuccessful) {
                DomainEventState.Success(response.body()!!.map { item-> eventItemMapper.map(item) })
            } else {
                DomainEventState.Error(response.message())
            }
        } catch (e: Exception) {
            DomainEventState.Error(e.message ?: "Unknown error")
        }
    }
}