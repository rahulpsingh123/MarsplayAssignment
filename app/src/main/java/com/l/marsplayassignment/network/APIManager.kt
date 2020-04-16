package com.l.marsplayassignment.network

import com.l.marsplayassignment.network.MyLoggingInterceptor.provideOkHttpLogging
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class APIManager private constructor() {

    private val baseUrl = "https://api.github.com/"
    private fun createRetrofitService(): JoshApiClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(provideOkHttpLogging())
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(JoshApiClient::class.java)
    }

    private val service: JoshApiClient
        get() = createRetrofitService()

    @JvmSuppressWildcards
    private interface JoshApiClient {

        @Multipart
        @POST("search/repositories")
        fun createImagePost(@Part files: MutableList<MultipartBody.Part?>): Call<ResponseBody>

    }

    fun uploadImage(parts: MutableList<MultipartBody.Part?>): Call<ResponseBody> {
        return service.createImagePost(parts)
    }

    companion object {
        private var myInstance: APIManager? = null
        val instance: APIManager?
            get() {
                if (myInstance == null) {
                    myInstance =
                        APIManager()
                }
                return myInstance
            }
    }
}