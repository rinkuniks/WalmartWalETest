package com.example.walmartwaletest.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walmartwaletest.modals.APODResponse
import com.example.walmartwaletest.network.NetworkHelper
import com.example.walmartwaletest.repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val repository: MainActivityRepository): ParentViewModel() {

    var _response: MediatorLiveData<APODResponse> = MediatorLiveData()
        private set

    val response: MediatorLiveData<APODResponse>
        get() = _response

    fun getAPODResponse() {
        _response.addSource(repository.getMainActivityData()) {
            apiDataHandling(it, object : ApiListener {
                override fun onSuccess() {
                    _response.postValue(it.data!!)
                }

                override fun onError() {

                }

            })
        }
    }

}