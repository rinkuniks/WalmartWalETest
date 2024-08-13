package com.example.walmartwaletest.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.walmartwaletest.Utils.API_SUCCESS_CODE
import com.example.walmartwaletest.modals.GenericResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class ApiNetwork <T : GenericResponse> {

    open var mutableLiveData: MutableLiveData<T> = MutableLiveData()
    val apiResult: MediatorLiveData<NetworkHelper<T>> = MediatorLiveData()


    private suspend fun makeApiCall() {
        val response = call()
        if (response.isSuccessful) {
            handleResponse(response)
        } else {
            apiResult.postValue(
                NetworkHelper(
                    response.body(),
                    response.code(),
                    NetworkResponseType.FAILURE
                )
            )
        }
    }

    private fun handleResponse(response: Response<T>) {
        if (response.code() == API_SUCCESS_CODE) {
            apiResult.postValue(
                NetworkHelper(
                    response.body(),
                    response.code(),
                    NetworkResponseType.SUCCESS
                )
            )
        } else {
            apiResult.postValue(
                NetworkHelper.onFailure(
                    response.body(),
                    response.code(),)
            )
            Log.d("TAG", "handleResponse: ${apiResult}")
        }
    }

    abstract suspend fun call(): Response<T>

    fun getResultInfo(): LiveData<NetworkHelper<T>> {
        return apiResult
    }

    init {
        apiResult.postValue(
            NetworkHelper.onLoading(null,null)
        )
        CoroutineScope(Dispatchers.IO).launch {
            makeApiCall()
        }
    }

}