package com.tymi.utils

import android.content.Context
import android.view.View
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.tymi.R
import com.tymi.TYMIApp

object AdUtils {
    fun showBannerAd(adView: AdView) {
        adView.loadAd(getAdRequest())
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = View.VISIBLE
            }
        }
    }

    fun showInterstitialAd(context: Context) {
        val interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = context.getString(R.string.interstitial_ad_id)
        interstitialAd.loadAd(getAdRequest())
        interstitialAd.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                if (TYMIApp.isActivityShowing)
                    interstitialAd.show()
            }
        }
    }

    fun showRewardedVideoAd(context: Context) {
        val rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context)
        rewardedVideoAd.loadAd(context.getString(R.string.rewarded_video_ad_id), getAdRequest())
        rewardedVideoAd.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {

            }

            override fun onRewardedVideoAdLeftApplication() {

            }

            override fun onRewardedVideoAdLoaded() {
                if (TYMIApp.isActivityShowing)
                    rewardedVideoAd.show()
            }

            override fun onRewardedVideoAdOpened() {

            }

            override fun onRewarded(p0: RewardItem?) {

            }

            override fun onRewardedVideoStarted() {

            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {

            }
        }
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().addTestDevice("79B1B0CB9BEBB6122B2E0C9EB8B3851B").build()
    }
}