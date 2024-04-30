package playzone.tj.utils

import androidx.fragment.app.Fragment
import playzone.tj.MainActivity
import playzone.tj.R


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