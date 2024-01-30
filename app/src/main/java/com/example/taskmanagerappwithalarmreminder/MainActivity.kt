package com.example.taskmanagerappwithalarmreminder

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback


class MainActivity : AppCompatActivity() {

    private var appOpenAd: AppOpenAd? = null
    private var isAdDisplayed: Boolean = false

    private val AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}
        loadAppOpenAd()

        setContentView(R.layout.activity_main)
    }

    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            this,
            AD_UNIT_ID,
            adRequest,
            //AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            appOpenAdLoadCallback
        )
    }

    private val appOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
        override fun onAdLoaded(ad: AppOpenAd) {
            appOpenAd = ad // Initialize the appOpenAd property here
            appOpenAd!!.show(this@MainActivity)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            // Handle ad loading failure
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAdDisplayed) {
            // Do not show the ad if it's already displayed
            return
        }
        appOpenAd?.show(this) ?: run {
            // If the ad is null, load it again
            loadAppOpenAd()
        }
    }

}