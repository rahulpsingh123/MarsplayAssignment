package com.l.marsplayassignment.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.NonNull
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

}