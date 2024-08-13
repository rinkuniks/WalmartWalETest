package com.example.walmartwaletest.repository

import androidx.lifecycle.LiveData
import com.example.walmartwaletest.modals.APODResponse
import com.example.walmartwaletest.network.ApiNetwork
import com.example.walmartwaletest.network.NetworkHelper
import retrofit2.Response
import javax.inject.Inject

class MainActivityRepository
@Inject constructor(val service: MainActivityService) {
    fun getMainActivityData(): LiveData<NetworkHelper<APODResponse>> {
        val data = object : ApiNetwork<APODResponse>() {
            override suspend fun call(): Response<APODResponse> {
                return service.getMainActivityData()
            }

        }
        return data.apiResult
    }
}
