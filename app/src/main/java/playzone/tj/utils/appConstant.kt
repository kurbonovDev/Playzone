package playzone.tj.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playzone.tj.MainActivity
import playzone.tj.retrofit.MainAPI
import retrofit2.Retrofit

lateinit var APP_ACTIVITY: MainActivity

lateinit var retrofit: Retrofit

lateinit var client: OkHttpClient

lateinit var interceptor: HttpLoggingInterceptor

lateinit var mainApi: MainAPI

var baseUrl = "http://172.20.10.4:8080"

const val STORAGE_KEY = "my_storage"
const val TOKEN_KEY = "token"
const val LOGIN_KEY = "login"

