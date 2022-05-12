package com.masterlibs.basestructure.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.docxmaster.docreader.base.BaseActivity
import com.docxmaster.docreader.base.BaseFragment
import com.masterlibs.basestructure.R

class CommonActivity : BaseActivity() {
    private var fragment: BaseFragment? = null
    override val layoutId: Int
        get() = R.layout.act_common

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            val clazz = this.intent.getSerializableExtra("FRAGMENT") as Class<*>?
            val fragment: Fragment
            fragment = if (clazz != null) {
                clazz.newInstance() as Fragment
            } else {
                val className = this.intent.getStringExtra("FRAGMENT_NAME")
                Class.forName(className).newInstance() as Fragment
            }
            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(R.id.commonFrag, fragment)
            ft.commit()
        } catch (var5: Throwable) {

        }
    }

    fun getFragment(): BaseFragment? {
        return fragment
    }

    fun setFragment(fragment: BaseFragment?) {
        this.fragment = fragment
    }

    override fun initView() {
    }

    override fun addEvent() {
    }
}