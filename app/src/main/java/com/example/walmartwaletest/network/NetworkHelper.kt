package com.example.walmartwaletest.network

class NetworkHelper<T>(
    val data: T?, val code: Int?, val responseType: NetworkResponseType?
) {

    companion object {
        fun <T> onSuccess(data: T?, code: Int?): NetworkHelper<T> =
            NetworkHelper(data, code, NetworkResponseType.SUCCESS)

        fun <T> onFailure(data: T?, code: Int?): NetworkHelper<T> =
            NetworkHelper(data, code, NetworkResponseType.FAILURE)

        fun <T> onLoading(data: T?, code: Int?): NetworkHelper<T> =
            NetworkHelper(data, code, NetworkResponseType.LOADING)
    }
}

enum class NetworkResponseType {
    SUCCESS, FAILURE, LOADING
}
