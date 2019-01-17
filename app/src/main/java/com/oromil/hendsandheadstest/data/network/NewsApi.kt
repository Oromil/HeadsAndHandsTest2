package com.oromil.hendsandheadstest.data.network

import com.oromil.hendsandheadstest.data.entities.ResponseEntity
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Singleton

private const val API_KEY = "sCokq6GJM4tSgdbDw0IUSSlBNUZ67hHR"
private const val BASE_URL = "https://api.nytimes.com/svc/"

@Singleton
interface NewsApi {

    @GET("topstories/v2/home.json?api-key=$API_KEY")
    fun getNews(): Observable<ResponseEntity>

    companion object Creator {
        fun create(): NewsApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(NewsApi::class.java)
        }
    }
}