package com.masterlibs.basestructure

import com.common.control.MyApplication

class App : MyApplication() {
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