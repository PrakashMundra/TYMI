package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tymi.AppPreferences
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.Profile
import com.tymi.interfaces.ISplashActivity
import com.tymi.utils.JSonUtils
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : BaseFragment() {
    private var iSplashActivity: ISplashActivity? = null

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ISplashActivity) {
            iSplashActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        iSplashActivity = null
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadeInAnimation.duration = Constants.SPLASH_TIME_OUT
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val userProfile = getAppPreferences().getString(AppPreferences.USER_PROFILE)
                if (userProfile.isNullOrEmpty())
                    iSplashActivity?.showLogin()
                else {
                    val profile = JSonUtils.fromJson(userProfile, Profile::class.java)
                    getDataModel().profile = profile
                    iSplashActivity?.showDashBoard()
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
        splash_title?.startAnimation(fadeInAnimation)
    }
}