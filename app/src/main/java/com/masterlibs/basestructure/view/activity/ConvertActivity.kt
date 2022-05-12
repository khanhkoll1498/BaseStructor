package com.masterlibs.basestructure.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import com.docxmaster.docreader.base.BaseActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.act_convert.*
import java.io.File
import java.io.FileOutputStream


class ConvertActivity(override val layoutId: Int = R.layout.act_convert) : BaseActivity() {
    var bitmap: Bitmap? = null
    var boolean_save: Boolean? = null
    val boolean_permission: Boolean? = null

    override fun initView() {
        bt_select.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_PICTURE)
        }
    }

    private fun createPDF() {
        val targetPdf = "/sdcard/test.pdf"
        val filePath = File(targetPdf)

        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(filePath))
        document.open()
        val image: Image = Image.getInstance("yourImageHere.jpg")
        document.add(Paragraph("Your Heading for the Image Goes Here"))
        document.add(image)
        document.close()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICTURE) {
            val selectedImage = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(
                selectedImage!!, filePathColumn, null, null, null
            )
            cursor!!.moveToFirst()

            val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
            val filePath = cursor!!.getString(columnIndex)
            cursor!!.close()


            bitmap = BitmapFactory.decodeFile(filePath)
        }

    }

    override fun addEvent() {

    }

    companion object {
        const val GALLERY_PICTURE = 1
        const val REQUEST_PERMISSION = 1
    }
}