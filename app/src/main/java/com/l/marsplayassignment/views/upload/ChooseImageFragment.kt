package com.l.marsplayassignment.views.upload

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.l.marsplayassignment.R
import com.l.marsplayassignment.helper.FileUtils
import com.l.marsplayassignment.helper.Utilities
import com.l.marsplayassignment.helper.Utilities.convertToFile
import com.l.marsplayassignment.helper.click
import com.l.marsplayassignment.viewModel.UploadViewModel
import com.l.marsplayassignment.views.baseView.BaseFragment
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import kotlinx.android.synthetic.main.fragment_choose_image.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ChooseImageFragment : BaseFragment() {
    private var model: UploadViewModel? = null

    private var mCaptureTime: Long = 0
    private val FOLDER_IMAGE_REQUEST = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProviders.of(requireActivity()).get(UploadViewModel::class.java)
        return inflater.inflate(R.layout.fragment_choose_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        camera?.setLifecycleOwner(this)
        camera?.addCameraListener(Listener())

        btn_capture.click { capturePicture() }
        btn_switch_camera.click { toggleCamera() }

        btn_flash.click {
            when (camera.flash) {
                Flash.TORCH -> {
                    camera.flash = Flash.OFF
                    btn_flash.setImageResource(R.drawable.ic_flash_off)
                }
                Flash.OFF -> {
                    btn_flash.setImageResource(R.drawable.ic_flash_on)
                    camera.flash = Flash.TORCH
                }
                Flash.ON -> {
                    btn_flash.setImageResource(R.drawable.ic_flash_off)
                    camera.flash = Flash.OFF
                }
                else -> {
                }
            }
        }

        arrow_gallery.click {
            openImageIntent()
        }
    }

    private fun openImageIntent() {
        val target = FileUtils.createGetContentIntent();
        val intent = Intent.createChooser(target, getString(R.string.app_name))
        try {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, FOLDER_IMAGE_REQUEST)
        } catch (e: ActivityNotFoundException) {
            // The reason for the existence of aFileChooser
        }
    }


    private fun capturePicture() {
        if (camera.mode == Mode.VIDEO) {
            return
        }
        if (camera.isTakingPicture) return
        mCaptureTime = System.currentTimeMillis()
        camera.takePicture()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && FOLDER_IMAGE_REQUEST == requestCode) {
            val uri = data?.data
            val id = DocumentsContract.getDocumentId(uri)
            val inputStream = activity?.contentResolver?.openInputStream(uri!!)
            val file =  File(activity?.cacheDir?.absolutePath +"/"+id)
            inputStream?.let { model?.writeFile(it, file) }
            if (file.length() <= 0) {
                file.deleteOnExit()
                return
            }
            model?.file = file
        }
    }

    private fun toggleCamera() {
        if (camera.isTakingPicture) return
        when (camera?.toggleFacing()) {
            Facing.BACK -> {
            }
            Facing.FRONT -> {
            }
        }
    }

    //region Permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var valid = true
        for (grantResult in grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED
        }
        if (valid && !camera.isOpened) {
            camera.open()
        }
    }


    private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
        }
        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            result.toBitmap(1000, 1000) { bitmap ->
                //create a file to write bitmap data
                model?.file =  convertToFile(bitmap, context!!)
            }
        }
    }

}