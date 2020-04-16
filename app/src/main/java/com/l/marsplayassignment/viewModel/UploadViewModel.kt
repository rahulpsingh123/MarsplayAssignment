package com.l.marsplayassignment.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import java.io.*

class UploadViewModel : BaseViewModel() {
    private val repo = UploadRepo()
    var state = MutableLiveData<ViewState>()

    enum class ViewState {
        PICK_IMAGE,
        CROP_IMAGE,
        UPLOAD_IMAGE,
    }

    var file: File? = null
        set(value) {
            field = value
            state.value = ViewState.CROP_IMAGE
        }

    var bitmap: Bitmap? = null
        set(value) {
            field = value
            state.value = ViewState.UPLOAD_IMAGE
        }

    fun writeFile(input: InputStream, file: File) {

        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int = 0
            while (input.read(buf).also { len = it } > 0) {
                out.run { write(buf, 0, len) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun uploadImage(parts: MutableList<MultipartBody.Part?>) {
        repo.uploadImage(parts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {

                })
                .let { subscriptions.add(it) }


    }
}