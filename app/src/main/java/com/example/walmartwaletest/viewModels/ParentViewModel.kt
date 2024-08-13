package com.example.walmartwaletest.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walmartwaletest.Utils.API_SOCKET_TIME_OUT
import com.example.walmartwaletest.Utils.API_SUCCESS_CODE
import com.example.walmartwaletest.modals.GenericResponse
import com.example.walmartwaletest.network.NetworkHelper
import com.example.walmartwaletest.network.NetworkResponseType

open class ParentViewModel() : ViewModel() {

    var apiError: MutableLiveData<String> = MutableLiveData()
    var viewError: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun apiDataHandling(networkHelper: NetworkHelper<out GenericResponse>, apiListener: ApiListener) {
        when (networkHelper.responseType) {

            NetworkResponseType.LOADING -> {
                isLoading.postValue(true)
            }

            NetworkResponseType.SUCCESS -> {
                isLoading.postValue(false)
                apiListener.onSuccess()
            }

            NetworkResponseType.FAILURE -> {
                isLoading.postValue(false)
                when (networkHelper.code) {
                    API_SUCCESS_CODE -> {
                        apiListener.onSuccess()
                    }
                    API_SOCKET_TIME_OUT -> {
                        apiError.value = "#800, something went wrong,please try after some time"
                    }

                    else -> {
                        apiError.value = "Please try after some time, something went wrong!!"
                    }
                }


            }

            else -> {

            }
        }
    }


    interface ApiListener {
        fun onSuccess()
        fun onError()
    }
}