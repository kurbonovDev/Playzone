package playzone.tj

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import playzone.tj.databinding.ActivityMainBinding
import playzone.tj.ui.main.PointFragment
import playzone.tj.ui.main_screen.MainFragment
import playzone.tj.ui.registration.ChooseGenreFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.replaceFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        APP_ACTIVITY = this
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (sharedPreferences.getBoolean("isRegistered", false)) {
            if (sharedPreferences.getBoolean("isChosenGenre", false)) {
                replaceFragment(PointFragment(), false)
            } else {
                replaceFragment(ChooseGenreFragment(), false)
            }
        } else {
            replaceFragment(MainFragment(), false)
        }


    }


}