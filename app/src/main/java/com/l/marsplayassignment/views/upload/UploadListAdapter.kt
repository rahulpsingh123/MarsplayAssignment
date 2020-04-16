package com.l.marsplayassignment.views.upload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ablanco.zoomy.Zoomy
import com.google.firebase.database.DataSnapshot
import com.l.marsplayassignment.R
import com.l.marsplayassignment.model.Image
import com.l.marsplayassignment.views.UploadListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_upload_list.view.*


class UploadListAdapter(private val fragment: UploadListFragment) : RecyclerView.Adapter<UploadListAdapter.ViewHolder>() {
    private val mImageList : MutableList<Image?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_upload_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val builder: Zoomy.Builder = Zoomy.Builder(fragment.activity).target(holder.itemView.image)
        builder.register()

        Picasso.with(fragment.activity)
            .load(mImageList[position]?.imageUrl)
            .placeholder(R.drawable.ic_picture)
            .fit()
            .centerCrop()
            .into(holder.itemView.image)
    }

    fun setData(dataSnapshot: DataSnapshot) {
        mImageList.clear()
        for (snapshot in dataSnapshot.children) {
            val upload: Image? = snapshot.getValue(Image::class.java)
            upload?.key = (snapshot.key)
            mImageList.add(upload)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = mImageList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}