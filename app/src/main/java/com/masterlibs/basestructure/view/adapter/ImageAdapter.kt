package com.masterlibs.basestructure.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.documentmaster.documentscan.Const
import com.documentmaster.documentscan.OnActionCallback
import com.masterlibs.basestructure.databinding.ButtonCameraBinding
import com.masterlibs.basestructure.databinding.ItemImageBinding

class ImageAdapter(private var mList: List<String?>?, private var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mCallback: OnActionCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return ButtonViewHolder(
                ButtonCameraBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        } else {
            return GalleryViewHolder(
                ItemImageBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }

    }


    private inner class GalleryViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(string: String) {
            itemView.tag = string
            Glide.with(context).load(string).into(binding.ivImage)
        }

        init {
            itemView.setOnClickListener { v: View? ->
                if (mCallback != null) {
                    mCallback?.callback(Const.CLICK_ITEM, itemView.tag)
                }
            }
        }
    }

    private inner class ButtonViewHolder(private val binding: ButtonCameraBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun loadData(string: String) {

        }

        init {
            itemView.setOnClickListener { v: View? ->
                mCallback!!.callback(Const.CAMERA, null)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val viewHolder1 = holder as GalleryViewHolder
                mList?.get(position)?.let { viewHolder1.loadData(it) }
            }
            0 -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList!!.get(position) == null) TYPE_BUTTON else TYPE_LIST_ITEM
    }

    companion object {
        const val TYPE_BUTTON = 0
        const val TYPE_LIST_ITEM = 1
    }

}