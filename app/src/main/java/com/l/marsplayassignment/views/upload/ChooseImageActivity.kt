package com.l.marsplayassignment.views.upload

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.l.marsplayassignment.R
import com.l.marsplayassignment.viewModel.UploadViewModel
import com.l.marsplayassignment.views.baseView.BaseActivity

class ChooseImageActivity : BaseActivity() {
    private var model : UploadViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_image)
        model = ViewModelProviders.of(this).get(UploadViewModel::class.java)

        addFragment(ChooseImageFragment(), addToBackStack = false, transition = TRANSITION.NONE)

        model?.state?.observe(this, Observer {
            when (it ?: "") {
                UploadViewModel.ViewState.CROP_IMAGE -> {
                    addFragment(CropImageFragment(), addToBackStack = true, transition = TRANSITION.NONE)
                }

                UploadViewModel.ViewState.UPLOAD_IMAGE -> {
                    addFragment(PreviewAndUploadFragment(), addToBackStack = true, transition = TRANSITION.NONE)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}