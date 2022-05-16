package com.masterlibs.basestructure.task

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.documentmaster.documentscan.OnActionCallback
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
class ImageToPdf(
    private val imagesUri: MutableList<String>,
    private val context: Context,
    private val callback: OnActionCallback
) : AsyncTask<String?, String?, String?>() {
    private val dialog: ProgressDialog?
    private var path: String? = null
    override fun onPreExecute() {
        super.onPreExecute()
        dialog!!.show()
    }

    override fun doInBackground(vararg params: String?): String? {
        path = params[0]
        val totalSize = totalSize
        var currentConvert: Long = 0
        val document = Document(PageSize.A4, 38F, 38F, 50F, 38F)
        val documentRect = document.pageSize
        try {
            PdfWriter.getInstance(document, FileOutputStream(path))
            document.open()
            for (i in imagesUri.indices) {
                var bmp: Bitmap
                bmp = BitmapFactory.decodeFile(imagesUri[i])
                val stream = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.PNG, 80, stream)
                currentConvert += File(imagesUri[i]).length()
                val percent = (currentConvert * 1.0 / totalSize * 100).toInt()
                publishProgress("$percent%", imagesUri[i])
                val image = Image.getInstance(stream.toByteArray())
                if (bmp.width > documentRect.width || bmp.height > documentRect.height) {
                    image.scaleToFit(documentRect.width - 16, documentRect.height - 16)
                } else {
                    image.scaleToFit(bmp.width.toFloat(), bmp.height.toFloat())
                }
                image.setAbsolutePosition(
                    (documentRect.width - image.scaledWidth) / 2,
                    (documentRect.height - image.scaledHeight) / 2
                )
                image.border = Image.BOX
                document.add(image)
                document.newPage()
            }
            document.close()
            val file = File(path)
        } catch (e: Exception) {
            Log.d("TAG", "doInBackground: " + e.message)
            e.printStackTrace()
        }
        imagesUri.clear()
        return null
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        val percent = values[0]
        dialog!!.setMessage("Creating...$percent")
    }

    private val totalSize: Long
        private get() {
            var rs: Long = 0
            for (i in imagesUri.indices) {
                rs += File(imagesUri[i]).length()
            }
            return rs
        }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        if ((context as Activity).isDestroyed) return
        dismissProgressDialog()
        try {
            callback.callback(null, path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissProgressDialog() {
        if (dialog != null && dialog.isShowing) dialog.dismiss()
    }

    init {
        dialog = ProgressDialog(context)
        dialog.setCancelable(false)
        dialog.setMessage("Creating... 0%")
    }
}