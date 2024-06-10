package playzone.tj.data.mapper

import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.domain.mapper.DataMapper
import playzone.tj.domain.models.DomainEventDTO

class EventItemMapper : DataMapper<EventDTO, DomainEventDTO> {
    override fun map(data: EventDTO): DomainEventDTO {
        return DomainEventDTO(
            eventId =data.eventId,
            gameId = data.gameId,
            eventName = data.eventName,
            title = data.title,
            creatorName = data.creatorName,
            imagePreview = data.imagePreview,
            imageMain = data.imageMain,
            imageCreator = data.imageCreator,
            description = data.description,
            status = data.status,
            format = data.format,
            watcherCount = data.watcherCount,
            link = data.link
        )
    }
}