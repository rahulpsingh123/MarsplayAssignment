package com.l.marsplayassignment.views.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.l.marsplayassignment.views.MainActivity
import com.l.marsplayassignment.R
import com.l.marsplayassignment.helper.Utilities
import com.l.marsplayassignment.helper.click
import com.l.marsplayassignment.helper.gone
import com.l.marsplayassignment.helper.show
import com.l.marsplayassignment.model.Image
import com.l.marsplayassignment.viewModel.UploadViewModel
import com.l.marsplayassignment.views.KEY
import com.l.marsplayassignment.views.baseView.BaseFragment
import kotlinx.android.synthetic.main.fragment_crop_image.toolbar
import kotlinx.android.synthetic.main.fragment_upload_image.*


class PreviewAndUploadFragment : BaseFragment() {
    private var model: UploadViewModel? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProviders.of(requireActivity()).get(UploadViewModel::class.java)
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.text_preview)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        ic_image.setImageBitmap(model?.bitmap)

        mStorageRef = FirebaseStorage.getInstance().getReference(KEY)
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(KEY)

        upload.click {
            it.isEnabled = false
            uploadFile(Uri.fromFile(Utilities.convertToFile(model?.bitmap, context!!)))
        }
    }

    private fun uploadFile(mImageUri: Uri) {
        val fileReference: StorageReference? = mStorageRef?.child(System.currentTimeMillis().toString() + ".png")
        profile_progressbar.show()

        fileReference?.putFile(mImageUri)
            ?.addOnSuccessListener { _ ->
                fileReference.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        val filename: String = mImageUri.toString().substring(mImageUri.toString().lastIndexOf("/") + 1)
                        val upload = Image(filename, url)
                        val uploadId: String = mDatabaseRef?.push()?.key ?: ""
                        mDatabaseRef?.child(uploadId)?.setValue(upload)
                    }

                profile_progressbar.gone()
                Toast.makeText(activity, " Upload successful", Toast.LENGTH_LONG).show()
                gotoMainActivity()
            }
            ?.addOnFailureListener { e ->
                upload.isEnabled = true
                profile_progressbar.gone()
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
            ?.addOnProgressListener { taskSnapshot ->
                val progress: Double =
                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                profile_progressbar?.progress = progress.toInt()

            }
    }

    private fun gotoMainActivity() {
        val mainActivityIntent = Intent(activity, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
    }

}