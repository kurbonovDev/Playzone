package playzone.tj

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playzone.tj.retrofit.MainAPI
import playzone.tj.ui.main.PointFragment
import playzone.tj.ui.main.games.GamesFragment
import playzone.tj.ui.main.home.HomeFragment
import playzone.tj.ui.main_screen.MainFragment
import playzone.tj.ui.registration.ChooseGenreFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.baseUrl
import playzone.tj.utils.client
import playzone.tj.utils.interceptor
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment
import playzone.tj.utils.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(500)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        APP_ACTIVITY = this

        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainApi = retrofit.create(MainAPI::class.java)

        if (sharedPreferences.getBoolean("isRegistered", false)) {
            if(sharedPreferences.getBoolean("isChosenGenre",false)){
                replaceFragment(PointFragment(),false)
            }else{
                replaceFragment(ChooseGenreFragment(),false)
            }
        }else{

            replaceFragment(MainFragment(), false)
        }

    }
}