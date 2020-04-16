package com.l.marsplayassignment.views

import android.content.Intent
import android.os.Bundle
import com.l.marsplayassignment.R
import com.l.marsplayassignment.helper.click
import com.l.marsplayassignment.views.baseView.BaseActivity
import com.l.marsplayassignment.views.upload.ChooseImageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white))


        addFragment(UploadListFragment(), addToBackStack = false, transition = TRANSITION.NONE)

        floating_button.click {
            startActivity(Intent(this, ChooseImageActivity::class.java))
        }
    }
}
