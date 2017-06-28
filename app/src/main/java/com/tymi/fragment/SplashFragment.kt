package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.tymi.AppPreferences
import com.tymi.R
import com.tymi.entity.Profile
import com.tymi.interfaces.ISplashActivity
import com.tymi.utils.JSonUtils

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
        Handler().postDelayed({
            val userProfile = getAppPreferences().getString(AppPreferences.USER_PROFILE)
            if (userProfile.isNullOrEmpty())
                iSplashActivity?.showLogin()
            else {
                val profile = JSonUtils.fromJson(userProfile, Profile::class.java)
                getDataModel().profile = profile
                iSplashActivity?.showDashBoard()
            }
        }, 1000)
    }
}