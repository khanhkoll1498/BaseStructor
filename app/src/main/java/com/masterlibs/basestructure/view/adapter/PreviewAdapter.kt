package com.masterlibs.basestructure.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.docxmaster.docreader.base.BaseAdapter
import com.masterlibs.basestructure.databinding.ItemPreviewPdfBinding

class PreviewAdapter(mList: MutableList<String?>?, context: Context?) :
    BaseAdapter<String?>(mList, context!!) {

    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return GalleryViewHolder(
            ItemPreviewPdfBinding.inflate(
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

    private inner class GalleryViewHolder(private val binding: ItemPreviewPdfBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(string: String) {
            Glide.with(context).load(string).into(binding.ivPreviewPdf)
        }

        init {
            itemView.setOnClickListener { v: View? ->

            }
        }
    }
}