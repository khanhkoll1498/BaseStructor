package com.masterlibs.basestructure.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.documentmaster.documentscan.Const
import com.docxmaster.docreader.base.BaseAdapter
import com.masterlibs.basestructure.databinding.ItemImageSelectedBinding

class ImageSelectedAdapter(mList: MutableList<String>?, context: Context?) :
    BaseAdapter<String>(mList, context!!) {

    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return ImageSelectedViewHolder(
            ItemImageSelectedBinding.inflate(
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
        val holder = viewHolder as ImageSelectedViewHolder
        mList?.get(position)?.let { holder.loadData(it) }
    }

    private inner class ImageSelectedViewHolder(private val binding: ItemImageSelectedBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(string: String) {
            itemView.tag = string
            Glide.with(context).load(string).into(binding.ivImage)
        }

        init {
            itemView.setOnClickListener { v: View? ->

            }

            binding.btClear.setOnClickListener { v: View? ->
                removeAt(adapterPosition)

                if (mList!!.size == 0) {
                    mCallback!!.callback(Const.SIZE, null)
                }
            }

        }
    }

    fun removeAt(position: Int) {
        mList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList!!.size)
    }

    companion object {
        const val EXTRA_ITEMS_ON_TOP = 1
        const val TYPE_BUTTON = 0
        const val TYPE_LIST_ITEM = 1
    }
}