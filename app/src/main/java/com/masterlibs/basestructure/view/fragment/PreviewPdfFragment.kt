package com.masterlibs.basestructure.view.fragment

import android.os.Bundle
import android.view.View
import com.documentmaster.documentscan.Const
import com.docxmaster.docreader.base.BaseFragment
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.view.adapter.PreviewAdapter
import kotlinx.android.synthetic.main.frg_preview.*

class PreviewPdfFragment(override val layoutId: Int = R.layout.frg_preview) : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataBundle()
    }

    override fun initView() {
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun addEvent() {

    }

    private fun getDataBundle() {
        val intent = requireActivity().intent
        if (intent.extras != null) {
            val listPath = intent.getStringArrayListExtra(Const.KEY_PREVIEW_PDF)

            val previewAdapter = PreviewAdapter(listPath, context)

            rc_preview.adapter = previewAdapter
        }
    }
}