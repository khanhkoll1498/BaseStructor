package com.masterlibs.basestructure.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.documentmaster.documentscan.Const
import com.docxmaster.docreader.base.BaseAdapter
import com.masterlibs.basestructure.databinding.ItemFolderImageBinding
import com.masterlibs.basestructure.model.Bucket
import com.masterlibs.basestructure.utils.ToastUtils

class AlbumsAdapter(mList: MutableList<Bucket?>?, context: Context?) :
    BaseAdapter<Bucket?>(mList, context!!) {

    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return GalleryViewHolder(
            ItemFolderImageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as GalleryViewHolder
        mList?.get(position)?.let { holder.loadData(it) }
    }

    private inner class GalleryViewHolder(private val binding: ItemFolderImageBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(bucket: Bucket) {
            Glide.with(context).load(bucket.firstImageContainedPath).into(binding.ivFirstImage)
            binding.tvFolderImage.text = bucket.bucketPath
            binding.tvCount.text = bucket.size.toString()
            itemView.tag = bucket
        }

        init {
            itemView.setOnClickListener { v: View? ->
                if (mCallback != null) {
                    mCallback?.callback(Const.BUCKET, itemView.tag)
                    ToastUtils.showToast(context, "Hello")

                }
            }
        }
    }
}