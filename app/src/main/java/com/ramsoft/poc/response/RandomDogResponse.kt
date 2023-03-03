package com.ramsoft.poc.response

import com.google.gson.annotations.SerializedName

data class RandomDogResponse(
    @SerializedName("message")
    val dogImage: String,
    @SerializedName("status")
    val status: String
)
