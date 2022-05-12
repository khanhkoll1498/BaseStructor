package com.masterlibs.basestructure.view.activity

import com.documentmaster.documentscan.Const
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.activity_view_pdf.*
import kotlinx.android.synthetic.main.frg_preview.iv_back
import java.io.File


class ViewPdfActivity(override val layoutId: Int = R.layout.activity_view_pdf) : BaseActivity() {
    override fun initView() {
        getDataBundle()

        iv_back.setOnClickListener { onBackPressed() }
    }

    override fun addEvent() {

    }

    private fun getDataBundle() {
        val intent = intent
        if (intent.extras != null) {
            val path = intent.getStringExtra(Const.KEY_VIEW_PDF)
            readFile(path as String)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun readFile(path: String) {

//        pdf_viewer.fromAsset(path)
//            .password(null)
//            .defaultPage(4)
//            .enableSwipe(true)
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .enableAnnotationRendering(true).load()

        val file = File(path)

        pdf_viewer.fromFile(file).load()
    }
}