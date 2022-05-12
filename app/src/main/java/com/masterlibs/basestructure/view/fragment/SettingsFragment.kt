package com.masterlibs.basestructure.view.fragment

import com.docxmaster.docreader.base.BaseFragment
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.utils.CommonUtils
import kotlinx.android.synthetic.main.frg_settings.*

class SettingsFragment(override val layoutId: Int = R.layout.frg_settings) : BaseFragment() {
    override fun initView() {
        iv_back.setOnClickListener { activity?.onBackPressed() }

        bt_share_app.setOnClickListener { CommonUtils.getInstance().shareApp(activity) }

        bt_privacy_policy.setOnClickListener { CommonUtils.getInstance().showPolicy(activity) }
    }

    override fun addEvent() {

    }
}