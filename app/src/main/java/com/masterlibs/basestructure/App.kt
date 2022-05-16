package com.masterlibs.basestructure

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.common.control.MyApplication
import com.masterlibs.basestructure.db.RoomDatabase
import com.masterlibs.basestructure.model.Bucket
import java.io.File
import java.text.SimpleDateFormat

class App : MyApplication() {

    companion object {
        var instance: App? = null
        var database: RoomDatabase? = null
        var buckets: MutableList<Bucket?> = ArrayList()

        private const val DATE_PATTERN = "ddMMyyyy"

        @SuppressLint("SimpleDateFormat")
        val formatDate = SimpleDateFormat(App.DATE_PATTERN)

        private const val DATE_TIME_PATTERN = "ddMMyyyy'_'HHmmss"

        @SuppressLint("SimpleDateFormat")
        val formatDateTime = SimpleDateFormat(App.DATE_TIME_PATTERN)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = RoomDatabase.getDatabase(instance)
        //getImageBuckets(this)
    }


//    @SuppressLint("Range")
//    private fun getImageBuckets(mContext: Context): List<Bucket?>? {
//        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection =
//            arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
//        val cursor: Cursor =
//            mContext.applicationContext.contentResolver.query(
//                uri,
//                projection,
//                null,
//                null,
//                null
//            )!!
//        if (cursor != null) {
//            var file: File
//            while (cursor.moveToNext()) {
//
//                try {
//                    val bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]))
//                    val firstImage = cursor.getString(cursor.getColumnIndex(projection[1]))
//                    file = File(firstImage)
//
//                    if (file.exists() && checkNameImage(buckets, string = bucketPath)) {
//                        buckets.add(Bucket(bucketPath, firstImage))
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//            cursor.close()
//        }
//
//        for (bucket in buckets) {
//
//            try {
//                val paths = getImagesByBucket(bucketPath = bucket!!.bucketPath) as List<String?>
//                bucket.paths = paths
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return buckets
//    }
//
//    @SuppressLint("Range")
//    private fun getImagesByBucket(bucketPath: String): List<String>? {
//        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
//        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
//        val images: MutableList<String> = ArrayList()
//        val cursor: Cursor = this.contentResolver
//            .query(uri, projection, selection, arrayOf(bucketPath), orderBy)!!
//        if (cursor != null) {
//            var file: File
//            while (cursor.moveToNext()) {
//                val path = cursor.getString(cursor.getColumnIndex(projection[0]))
//                file = File(path)
//                if (file.exists() && !images.contains(path)) {
//                    images.add(path)
//                }
//            }
//            cursor.close()
//        }
//        return images
//    }
//
//    private fun checkNameImage(mutableList: MutableList<Bucket?>, string: String): Boolean {
//        for (name in mutableList) {
//            if (name!!.bucketPath == null && name.bucketPath == string) {
//                return false
//            }
//        }
//        return true
//    }


    override fun isPurchased(): Boolean {
        return BuildConfig.PURCHASED
    }

    override fun isShowAdsTest(): Boolean {
        return BuildConfig.DEBUG || BuildConfig.TEST_AD
    }

    override fun enableAdsResume(): Boolean {
        return false
    }

    override fun getOpenAppAdId(): String? {
        return null
    }
}