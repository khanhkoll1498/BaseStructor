package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.documentmaster.documentscan.Const
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.model.Bucket
import com.masterlibs.basestructure.utils.ToastUtils
import com.masterlibs.basestructure.view.adapter.ImageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream


class TestActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity(),
    OnActionCallback {

    var mList: MutableList<String> = arrayListOf()

    var nameFolder: String? = null
    var bid: String? = null
    var mListName: MutableList<String> = arrayListOf()

    private var mListData: MutableList<String?>? = null
    private var imageAdapter: ImageAdapter? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            { isGranted: Boolean ->
                if (isGranted) {

                } else {

                }
            }
        }

    override fun initView() {
        requestPer()

        //getListPathFolder()

        initData()

        bt_add.setOnClickListener {
            try {

                val targetPdf = mList.toString()
                val filePath = File(targetPdf)

                val path = Environment.getExternalStorageDirectory().toString()
                val document = Document()

                PdfWriter.getInstance(document, FileOutputStream(filePath))



                document.open()
                val image = Image.getInstance("$mList/example.png")
                document.add(Paragraph("Heading for the Image"))
                document.add(image)
                document.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        getImageBuckets(this)
    }

    private fun getListPathFolder() {
        val column = MediaStore.Images.Media.BUCKET_ID
        val projection = arrayOf(column)

        mListData = java.util.ArrayList()

        val cursor = applicationContext.contentResolver.query(
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
        Log.i(TAG, "getListPathFolder: ${mListData!!.size}")
    }

    @SuppressLint("Range")
    fun getImageBuckets(mContext: Context): List<Bucket>? {
        val buckets: MutableList<Bucket> = ArrayList()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            applicationContext.contentResolver.query(uri, projection, null, null, null)!!
        if (cursor != null) {
            var file: File
            while (cursor.moveToNext()) {
                val bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]))
                val firstImage = cursor.getString(cursor.getColumnIndex(projection[1]))
                file = File(firstImage)

                if (file.exists() && checkNameImage(buckets, string = bucketPath)) {
                    //buckets.add(Bucket(bucketPath, firstImage))
                }
            }
            cursor.close()
        }

        for (bucket in buckets) {
            val path = getImagesByBucket(bucketPath = bucket.bucketPath)
            Log.i(TAG, "initView: $path")
        }
        return buckets
    }

    private fun checkNameImage(mutableList: MutableList<Bucket>, string: String): Boolean {
        for (name in mutableList) {
            if (name.bucketPath == string) {
                return false
            }
        }
        return true
    }

    @SuppressLint("Range")
    fun getImagesByBucket(bucketPath: String): List<String>? {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val images: MutableList<String> = ArrayList()
        val cursor: Cursor = applicationContext.contentResolver
            .query(uri, projection, selection, arrayOf(bucketPath), orderBy)!!
        if (cursor != null) {
            var file: File
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndex(projection[0]))
                file = File(path)
                if (file.exists() && !images.contains(path)) {
                    images.add(path)
                }
            }
            cursor.close()
        }
        return images
    }

    @SuppressLint("Recycle", "UseCompatLoadingForDrawables")
    private fun initData() {
        val column = "_data"
        val projection = arrayOf(column)

        mListData = java.util.ArrayList()
        val cursor = applicationContext.contentResolver.query(
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
    }

    private fun requestPer() {
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

    override fun addEvent() {

    }

    override fun callback(key: String?, vararg data: Any?) {
        if (Const.K_CLICK_ITEM == key) {
            val path = data[0] as String

            ToastUtils.showToast(this, path)

            mList.add(path)
            mList.size
        }
    }
}