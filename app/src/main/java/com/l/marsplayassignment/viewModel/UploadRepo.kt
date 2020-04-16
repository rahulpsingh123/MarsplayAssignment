package com.l.marsplayassignment.viewModel

import com.l.marsplayassignment.network.APIManager
import com.l.marsplayassignment.network.NetworkClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class UploadRepo :  BaseRepo() {

    fun uploadImage(parts: MutableList<MultipartBody.Part?>): Single<String> {
        return NetworkClient
            .getResult(APIManager.instance?.uploadImage(parts))
            .subscribeOn(Schedulers.io())
    }
}