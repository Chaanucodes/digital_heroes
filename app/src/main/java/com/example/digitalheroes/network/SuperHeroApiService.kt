package com.example.digitalheroes.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://superheroapi.com/api/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface SuperHeroApiService{
//    @GET("2871462739575627/{id}/powerstats")
//    fun getSuperheroes(@Path("id")id : Int) : Deferred<SuperHeroes>
//
//    @GET("2871462739575627/{id}/image")
//    fun getImage(@Path("id")id : Int) : Deferred<SuperImage>
//
//    @GET("2871462739575627/{id}/biography")
//    fun getBio(@Path("id")id : Int) : Deferred<SuperBio>
//
//    @GET("2871462739575627/{id}/connections")
//    fun getFamily(@Path("id")id : Int) : Deferred<SuperConnection>

    @GET("2871462739575627/search/{name}")
    fun getSuperHeroList(@Path("name") name : String) : Deferred<SuperHeroesList>

}

object SuperHeroApi{
    val retrofitService : SuperHeroApiService by lazy {
        retrofit.create(SuperHeroApiService::class.java)
    }
}

