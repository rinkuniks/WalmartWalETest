package com.example.walmartwaletest.interfaces

import com.example.walmartwaletest.modals.APODResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET("planetary/apod?api_key=DEMO_KEY")
    suspend fun getAPOD(): Response<APODResponse>
}