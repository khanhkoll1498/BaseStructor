package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.base.CommonActivity
import com.masterlibs.basestructure.base.Constants
import com.masterlibs.basestructure.model.Bucket
import com.masterlibs.basestructure.utils.ToastUtils
import com.masterlibs.basestructure.view.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bars_top_home.*
import java.io.File

class HomeActivity(override val layoutId: Int = R.layout.act_home_no_file) : BaseActivity() {
    var buckets: MutableList<Bucket>? = arrayListOf()

    override fun initView() {
        requestPermission()

        bt_add.setOnClickListener {
            ToastUtils.showToast(this, "Click on button add")
            gotoAddImageFragment()
        }

        iv_selectall.setOnClickListener {
            Toast.makeText(
                this,
                "Click on image select all",
                Toast.LENGTH_SHORT
            ).show()
        }

        iv_sort.setOnClickListener {
            Toast.makeText(
                this,
                "Click on image sort",
                Toast.LENGTH_SHORT
            ).show()
        }

        iv_setting.setOnClickListener {
            gotoSettingsFragment()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(this, "Permission Granted")
                    getImageBuckets(this)
                } else {
                    ToastUtils.showToast(this, "Permission Denied")
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun gotoAddImageFragment() {
//        val intent = Intent(this, AddImageActivity::class.java)
//        val bundle = Bundle()
//        //bundle.putSerializable(Constants.FRAGMENT, AddImageFragment::class.java)
//        intent.putExtras(bundle)
//        startActivity(intent)

        AddImageActivity.start(context = this, buckets as MutableList<Bucket>)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, HomeActivity::class.java)
            context.startActivity(starter)
        }

        //var buckets: MutableList<Bucket?> = ArrayList()
    }

    private fun gotoSettingsFragment() {
        val intent = Intent(this, CommonActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constants.FRAGMENT, SettingsFragment::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun addEvent() {

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        );
    }

    @SuppressLint("Range")
    private fun getImageBuckets(mContext: Context): List<Bucket?>? {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            mContext.applicationContext.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )!!
        if (cursor != null) {
            var file: File
            while (cursor.moveToNext()) {

                try {
                    val bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]))
                    val firstImage = cursor.getString(cursor.getColumnIndex(projection[1]))
                    file = File(firstImage)

                    if (file.exists() && checkNameImage(buckets!!, string = bucketPath)) {
                        buckets!!.add(Bucket(bucketPath, firstImage))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            cursor.close()
        }

        for (bucket in buckets!!) {

            try {
                val paths = getImagesByBucket(bucketPath = bucket!!.bucketPath) as List<String?>
                bucket.paths = paths
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return buckets
    }

    @SuppressLint("Range")
    private fun getImagesByBucket(bucketPath: String): List<String>? {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val images: MutableList<String> = ArrayList()
        val cursor: Cursor = this.contentResolver
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

    private fun checkNameImage(mutableList: MutableList<Bucket>, string: String): Boolean {
        for (name in mutableList) {
            if (name.bucketPath == string) {
                return false
            }
        }
        return true
    }
}