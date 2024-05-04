package playzone.tj

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playzone.tj.retrofit.MainAPI
import playzone.tj.ui.HomeFragment
import playzone.tj.ui.MainFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.client
import playzone.tj.utils.interceptor
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment
import playzone.tj.utils.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APP_ACTIVITY=this


        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

         retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.154.179:8080").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainApi = retrofit.create(MainAPI::class.java)
        replaceFragment(HomeFragment(),false)


    }
}