package playzone.tj.domain.mapper

interface DataMapper<Data,Domain> {
    fun map(data: Data): Domain
}