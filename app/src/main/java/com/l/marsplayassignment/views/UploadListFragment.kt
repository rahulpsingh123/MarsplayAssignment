package com.l.marsplayassignment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.l.marsplayassignment.R
import com.l.marsplayassignment.helper.gone
import com.l.marsplayassignment.helper.show
import com.l.marsplayassignment.views.baseView.BaseFragment
import com.l.marsplayassignment.UploadListAdapter
import kotlinx.android.synthetic.main.fragment_upload_list.*

const val KEY ="image_uploads"
class UploadListFragment : BaseFragment() {
    private var adapter = UploadListAdapter(this)
    private var mDatabaseRef: DatabaseReference? = null
    private var mDBListener: ValueEventListener? = null
    private var mStorage: FirebaseStorage? = null

    enum class VIEW_STATE { LOADING, LOADED, ERROR, EMPTY_LIST }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upload_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter

        mStorage = FirebaseStorage.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(KEY)

        setState(VIEW_STATE.LOADING)
        mDBListener = mDatabaseRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount == 0L) {
                    setState(VIEW_STATE.EMPTY_LIST)
                } else {
                    setState(VIEW_STATE.LOADED)
                }
                adapter.setData(dataSnapshot)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                setState(VIEW_STATE.ERROR)
            }
        })
    }

    private fun setState(state: VIEW_STATE) {
        when (state) {
            VIEW_STATE.LOADING -> {
                loader?.show()
                no_data_found?.gone()
            }
            VIEW_STATE.LOADED -> {
                loader?.gone()
                no_data_found?.gone()
            }
            VIEW_STATE.EMPTY_LIST -> {
                loader?.gone()
                no_data_found?.show()
            }
            VIEW_STATE.ERROR -> {
                no_data_found?.gone()
                loader?.gone()
                Toast.makeText(activity, " Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }
    }
}