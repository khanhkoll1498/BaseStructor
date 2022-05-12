package com.masterlibs.basestructure.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.model.ImagePath


class CustomAdapter(private var mList: MutableList<String>?, private var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val arrayList: ArrayList<ImagePath>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        if (viewType == ITEM_VIEW_ONE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_file_pdf, parent, false)

            return ViewHolder(view)
        } else if (viewType == ITEM_VIEW_TWO) {
            view = LayoutInflater.from(context).inflate(R.layout.button_camera, parent, false)

            return ButtonViewHolder(view)
        }
        return view as RecyclerView.ViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        val linkPath = arrayList!!.get(position)

        when (itemType) {
            ITEM_VIEW_ONE -> Glide.with(context).load(linkPath).into((holder as ViewHolder).ivImage)
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        if (arrayList!!.get(position).viewType == 0) {
            return ITEM_VIEW_ONE
        } else {
            return ITEM_VIEW_TWO
        }
    }

    companion object {
        const val ITEM_VIEW_ONE = 0
        const val ITEM_VIEW_TWO = 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView!!.findViewById(R.id.iv_image)
    }

    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCamera: ImageView = itemView!!.findViewById(R.id.iv_camera)
    }
}