package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.documentmaster.documentscan.Const
import com.documentmaster.documentscan.OnActionCallback
import com.documentmaster.documentscan.extention.setUserProperty
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.App
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.base.CommonActivity
import com.masterlibs.basestructure.base.Constants
import com.masterlibs.basestructure.model.Bucket
import com.masterlibs.basestructure.utils.ImageToPdf
import com.masterlibs.basestructure.view.adapter.AlbumsAdapter
import com.masterlibs.basestructure.view.adapter.ImageAdapter
import com.masterlibs.basestructure.view.adapter.ImageSelectedAdapter
import com.masterlibs.basestructure.view.dialog.DialogConfirmConvert
import com.masterlibs.basestructure.view.fragment.PreviewPdfFragment
import kotlinx.android.synthetic.main.act_add_image.*
import kotlinx.android.synthetic.main.activity_main.rc_image
import java.io.File


class AddImageActivity(override val layoutId: Int = R.layout.act_add_image) : BaseActivity(),
    OnActionCallback {

    private var mListData: MutableList<String?>? = null
    var mList: MutableList<String> = arrayListOf()
    var path: MutableList<String> = arrayListOf()
    var adapterImage: ImageAdapter? = null
    var dialogConfirmConvert: DialogConfirmConvert? = null
    var isClick: Boolean? = null
    var nameFile: String? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            { isGranted: Boolean ->
                if (isGranted) {

                } else {

                }
            }
        }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun initView() {
        initData()

        getData()

        ct_import.setOnClickListener { null }

        isClick = false
        ct_dialog.visibility = View.GONE

        view_not_click.setOnClickListener {
            ct_dialog.visibility = View.GONE
        }

        val external: Array<out File> =
            ContextCompat.getExternalFilesDirs(this.applicationContext, null)
        Log.i("TAG", "initView: $external")

        ll_all_images.setOnClickListener {
            gotoDialogFragment()
        }

        iv_back.setOnClickListener {
            this.onBackPressed()
        }

        bt_import.setOnClickListener {
            dialogConfirmConvert = DialogConfirmConvert()
            dialogConfirmConvert!!.show(this.supportFragmentManager, "Dialog")
            dialogConfirmConvert!!.mCallback = this
//            val imageToPdf = ImageToPdf(mList, context, this)
//            imageToPdf.execute()
//            Log.i("TAG", "initView: " + mList.size)
        }
    }

    private fun getData() {
        buckets = intent.getSerializableExtra(Const.ALBUMS) as MutableList<Bucket?>
    }

    private fun gotoDialogFragment() {
//        val galleryAdapter = AlbumsAdapter(App.buckets, context)
//        galleryAdapter.mCallback = this
//        rc_dialog_album.adapter = galleryAdapter
//
//        ct_file.setOnClickListener {
//            context?.setUserProperty("CLICK_File_Mgr")
//            val intent = Intent()
//            intent.type = "image/*"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Open_File_Mgr"), 1)
//        }

        if (ct_dialog.visibility == View.GONE) {
            ct_dialog.visibility = View.VISIBLE
            val galleryAdapter = AlbumsAdapter(buckets, this)
            galleryAdapter.mCallback = this
            rc_dialog_album.adapter = galleryAdapter

            ct_file.setOnClickListener {
                this?.setUserProperty("CLICK_File_Mgr")
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Open_File_Mgr"), 1)
            }
            isClick = true
        } else if (isClick == true && ct_dialog.visibility == View.VISIBLE) {
            ct_dialog.visibility = View.GONE
        }
    }

    override fun addEvent() {

    }

    private fun initData() {
        val column = "_data"
        val projection = arrayOf(column)

        mListData = java.util.ArrayList()
        val cursor = this.applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val index = cursor.getColumnIndexOrThrow(column)
                val path = cursor.getString(index)
                if (path != null && !path.isEmpty()) {
                    (mListData as java.util.ArrayList<String?>).add(path)
                }
                cursor.moveToNext()
            }
        }

        rc_image.layoutManager = GridLayoutManager(this@AddImageActivity, 3)
        mListData?.add(0, null)

        adapterImage = ImageAdapter(mListData, this)

        rc_image.adapter = adapterImage
        adapterImage!!.mCallback = this
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun callback(key: String?, vararg data: Any?) {
        when (key) {
            Const.CLICK_ITEM -> {
                showRCImageSelected(data)
            }

            Const.BUCKET -> {
                showAlbumsSelected(data)
            }

            Const.CAMERA -> {
                openCamera()
            }

            Const.PATH_PDF -> {
                viewFilePDF(data)
                Log.i("TAG", "callback: ..")
            }

            Const.KEY_CONVERT -> {
                convertFile(data)
            }

            Const.KEY_PREVIEW -> {
                previewFileConvert()
            }

            Const.SIZE -> {
                ct_import.visibility = View.GONE
            }
        }
    }

    private fun convertFile(data: Array<out Any?>) {
        dialogConfirmConvert!!.dismiss()
        val name = data[0] as String
        val imageToPdf = ImageToPdf(mList, this, this@AddImageActivity)
        imageToPdf.execute(name)
        Log.i("TAG", "initView: " + mList.size)
    }

    private fun previewFileConvert() {
        val intent = Intent(this, CommonActivity::class.java)
        val bundle = Bundle()
        bundle.putStringArrayList(Const.KEY_PREVIEW_PDF, mList as ArrayList<String>)
        bundle.putSerializable(Constants.FRAGMENT, PreviewPdfFragment::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun viewFilePDF(data: Array<out Any?>) {
        val path = data[0] as String
        val intent = Intent(this, ViewPdfActivity::class.java)
        intent.putExtra(Const.KEY_VIEW_PDF, path)
        startActivity(intent)
        finish()
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(Intent.createChooser(intent, "Open_Camera"), 1)
    }

    private fun showAlbumsSelected(data: Array<out Any?>) {
        val bucket = data[0] as Bucket
        Log.d("TAG", "callback: ")


        mListData!!.clear()

        mListData!!.addAll(bucket.paths)

        tv_title.text = bucket.bucketPath

        ct_dialog.visibility = View.GONE

        mListData?.add(0, null)

        adapterImage?.notifyDataSetChanged()

    }

    private fun showRCImageSelected(data: Array<out Any?>) {
        val path = data[0] as String

        mList.add(path)

        ct_import.visibility = View.VISIBLE

        rc_image_selected.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ImageSelectedAdapter(mList, this)
        rc_image_selected.adapter = adapter
        adapter.mCallback = this
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun start(context: Context, list: MutableList<Bucket>) {
            val starter = Intent(context, AddImageActivity::class.java)

            starter.putExtra(Const.ALBUMS, list as ArrayList)
            context.startActivity(starter)
        }

        var buckets: MutableList<Bucket?> = ArrayList()
    }

}