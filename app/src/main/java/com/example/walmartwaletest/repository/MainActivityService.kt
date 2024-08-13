package com.example.walmartwaletest.repository

import com.example.walmartwaletest.interfaces.ApiServices
import com.example.walmartwaletest.modals.APODResponse
import retrofit2.Response

import javax.inject.Inject

class MainActivityService
    @Inject constructor(val apiServices: ApiServices)  {
    suspend fun getMainActivityData(): Response<APODResponse> {
        return  apiServices.getAPOD()
    }
}