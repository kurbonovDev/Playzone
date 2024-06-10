package playzone.tj.domain.models



sealed class DomainEventState{
    data object Loading : DomainEventState()
    data class Success(val eventData: List<DomainEventDTO>) : DomainEventState()
    data class Error(val errorMessage: String) : DomainEventState()
    data object Empty : DomainEventState()
}

