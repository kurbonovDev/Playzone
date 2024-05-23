package playzone.tj.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playzone.tj.retrofit.models.TokenResponse
import playzone.tj.retrofit.models.login.LoginReceiveRemote
import playzone.tj.utils.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

object MainRetrofit{
    private fun createClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .client(createClient())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val mainApi = retrofit.create(MainAPI::class.java)

}
