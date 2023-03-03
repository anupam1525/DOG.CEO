package com.ramsoft.poc.repository

import com.ramsoft.poc.data.RetrofitClient

class DogRepository(private val retrofitClient: RetrofitClient) {

    fun getDogImage() = retrofitClient.getRandomDogImage()

}