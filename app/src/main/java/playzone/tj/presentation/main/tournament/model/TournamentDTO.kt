package playzone.tj.presentation.main.tournament.model

import androidx.annotation.DrawableRes

data class TournamentDTO(
    @DrawableRes
    val teamFirstImage: Int,

    @DrawableRes
    val teamSecondImage: Int,

    val teamFirstName:String,
    val teamSecondName:String,

    val teamFirstScore: String,
    val teamSecondScore: String,

    val teamFirstMoney:String,
    val teamSecondMoney:String,

    val watcherCount:String
)