package playzone.tj

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playzone.tj.databinding.ActivityMainBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.ui.main.PointFragment
import playzone.tj.ui.main_screen.MainFragment
import playzone.tj.ui.registration.ChooseGenreFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.baseUrl
import playzone.tj.utils.client
import playzone.tj.utils.interceptor
import playzone.tj.utils.isOnline
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment
import playzone.tj.utils.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        APP_ACTIVITY = this
        initRetrofit()
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

    private fun initRetrofit() {
        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainApi = retrofit.create(MainAPI::class.java)
    }
}