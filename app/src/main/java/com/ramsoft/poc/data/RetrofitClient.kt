package com.ramsoft.poc.data

import com.ramsoft.poc.response.RandomDogResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitClient {

    @GET("breeds/image/random")
    fun getRandomDogImage(): Call<RandomDogResponse>

    companion object {

        private var retrofitClient: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            if (retrofitClient == null) {
                var retrofit = Retrofit.Builder()
                    .baseUrl("https://dog.ceo/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitClient = retrofit.create(RetrofitClient::class.java)
            }
            return retrofitClient!!
        }
    }
}