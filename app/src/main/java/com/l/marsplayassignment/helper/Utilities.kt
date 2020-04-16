package com.l.marsplayassignment.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.NonNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utilities {
    @JvmStatic
    fun dateToString(date: Date?, format: String?): String {
        if (date == null) {
            return ""
        }
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

     fun convertToFile(bitmap: Bitmap?, context : Context) : File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(Date()).toLong()
        val imageFileName = timeStamp.toString() + "_mars"
        val file = File(context?.cacheDir, imageFileName)
        file.createNewFile()
        val bos = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return file
    }

    @NonNull
    fun prepareLookFilePart(
        fileUri: Uri?,
        context: Context?
    ): MultipartBody.Part? {

        val file: File = FileUtils.getFile(context, fileUri)
        val mimeType: String = FileUtils.getMimeType(file)
        val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("images_attributes[]", file.name, requestFile)
    }

    enum class ImageResolution(var data: Resolutions) {
        //for every image we decided to go with two resolutions lower and higher
        THUMBNAIL(Resolutions(400, 400, 400, 400)),

    }

    data class Resolutions(var lWidth: Int, var lHeight: Int, var hWidth: Int, var hHeight: Int)

}