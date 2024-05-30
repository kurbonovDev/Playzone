package playzone.tj.utils

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.retrofit.models.games.GameDTO


fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.placeHolder, fragment)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, fragment)
            .commit()
    }
}


fun filterGames(games: List<GameDTO>): Pair<List<GameDTO>, List<GameDTO>> {
    val sortedGames = games.sortedByDescending { it.downloadCount }
    val topThreeGames = sortedGames.take(3)
    val remainingGames = sortedGames.drop(3)
    return Pair(topThreeGames, remainingGames)
}




