package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.base.CommonActivity
import com.masterlibs.basestructure.base.Constants
import com.masterlibs.basestructure.utils.ToastUtils
import com.masterlibs.basestructure.view.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bars_top_home.*

class HomeActivity(override val layoutId: Int = R.layout.act_home_no_file) : BaseActivity() {
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

    private fun gotoAddImageFragment() {
//        val intent = Intent(this, AddImageActivity::class.java)
//        val bundle = Bundle()
//        //bundle.putSerializable(Constants.FRAGMENT, AddImageFragment::class.java)
//        intent.putExtras(bundle)
//        startActivity(intent)

        AddImageActivity.start(context = this)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, HomeActivity::class.java)
            context.startActivity(starter)
        }
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


}