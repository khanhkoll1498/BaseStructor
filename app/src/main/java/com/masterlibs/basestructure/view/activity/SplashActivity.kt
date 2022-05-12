package com.masterlibs.basestructure.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.masterlibs.basestructure.BuildConfig
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.model.Bucket
import java.io.File

class SplashActivity(override val layoutId: Int = R.layout.act_splash) : BaseActivity() {

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            AdmobManager.getInstance()
                .loadInterAds(
                    this@SplashActivity,
                    BuildConfig.inter_splash,
                    object : AdCallback() {
                        override fun interCallback(interstitialAd: InterstitialAd?) {
                            showInter(interstitialAd)
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            startMain()
                        }
                    })
        }, 2000)
    }

    private fun showInter(interstitialAd: InterstitialAd?) {
        if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            AdmobManager.getInstance()
                .showInterstitial(this, interstitialAd, object : AdCallback() {
                    override fun onAdClosed() {
                        startMain()
                    }

                })
        } else {
            startMain()
        }
    }

    private fun startMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun addEvent() {

    }
}