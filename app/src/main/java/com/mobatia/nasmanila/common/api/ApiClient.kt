package com.mobatia.nasmanila.api

import com.google.gson.GsonBuilder
import com.mobatia.nasmanila.common.api.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var BASE_URL ="http://gama.mobatia.in:8080/NAIS-Manila2023/public/"



    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)

        }
    /*private lateinit var apiService: ApiService

    //    private static final String BASE_URL = "https://beta.mobatia.in:8888/nas-dubai-development/";
    //    private static final String BASE_URL = "https://beta.mobatia.in:8888/nais-dubai-penetration-fix/";
    private const val BASE_URL = "http://gama.mobatia.in:8080/NAIS-Manila2023/public/"
    private val retrofit: Retrofit? = null

    //    private static final String BASE_URL = "https://cms.nasdubai.ae/nais/";
    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(" http://manila.mobatia.in:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }*/
}