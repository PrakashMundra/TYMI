package com.tymi.widget

import android.content.Context
import android.util.AttributeSet
import com.tymi.R
import com.tymi.utils.AdUtils
import kotlinx.android.synthetic.main.widget_banner_ad.view.*

class BannerAdWidget : BaseWidget {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayoutId(): Int {
        return R.layout.widget_banner_ad
    }

    override fun init() {
        super.init()
        AdUtils.showBannerAd(adView)
    }
}