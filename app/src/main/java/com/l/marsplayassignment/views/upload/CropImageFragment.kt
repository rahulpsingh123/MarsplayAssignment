package com.l.marsplayassignment.views.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.l.marsplayassignment.R
import com.l.marsplayassignment.helper.click
import com.l.marsplayassignment.viewModel.UploadViewModel
import com.l.marsplayassignment.views.baseView.BaseFragment
import kotlinx.android.synthetic.main.fragment_crop_image.*

class CropImageFragment : BaseFragment() {
    private var model: UploadViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProviders.of(requireActivity()).get(UploadViewModel::class.java)
        return inflater.inflate(R.layout.fragment_crop_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.text_crop)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))

        cropImageView.setImageUriAsync(Uri.fromFile(model?.file))

        next.click {
            model?.bitmap = cropImageView.croppedImage
        }
    }
}