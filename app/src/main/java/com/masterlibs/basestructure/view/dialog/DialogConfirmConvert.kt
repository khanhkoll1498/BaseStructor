package com.masterlibs.basestructure.view.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.documentmaster.documentscan.Const
import com.documentmaster.documentscan.OnActionCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.masterlibs.basestructure.App
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_confirm_convert.*
import java.util.*


class DialogConfirmConvert : BottomSheetDialogFragment() {
    var mCallback: OnActionCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.dialog_confirm_convert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        bt_preview.setOnClickListener {
            mCallback!!.callback(Const.KEY_PREVIEW, null)
        }

        val getDate = Calendar.getInstance().time

        val date = App.formatDateTime.format(getDate)

        edt_name.setText(edt_name.text.toString() + date)

        bt_convert.setOnClickListener {
            mCallback!!.callback(Const.KEY_CONVERT, edt_name.text.toString())
        }
    }
}