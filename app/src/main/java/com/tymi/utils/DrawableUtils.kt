package com.tymi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatDrawableManager
import android.widget.TextView

class DrawableUtils {
    companion object {
        fun setCompoundDrawableLocale(view: TextView, @DrawableRes drawableId: Int) {
            setCompoundDrawable(view, drawableId, 0, 0, 0)
        }

        fun setCompoundDrawableLocaleInverse(view: TextView, @DrawableRes drawableId: Int) {
            setCompoundDrawable(view, 0, 0, drawableId, 0)
        }

        private fun setCompoundDrawable(view: TextView?, @DrawableRes left: Int,
                                        @DrawableRes top: Int, @DrawableRes right: Int, @DrawableRes bottom: Int) {
            if (view == null) return
            val current = view.compoundDrawables
            val ctx = view.context
            val leftDr = getDrawableFallback(ctx, left, current, 0)
            val topDr = getDrawableFallback(ctx, top, current, 1)
            val rightDr = getDrawableFallback(ctx, right, current, 2)
            val bottomDr = getDrawableFallback(ctx, bottom, current, 3)
            view.setCompoundDrawablesWithIntrinsicBounds(leftDr, topDr, rightDr, bottomDr)
        }

        private fun getDrawableFallback(ctx: Context, @DrawableRes res: Int, fallbacks: Array<Drawable>, index: Int): Drawable? {
            if (res == 0) return null
            if (res == -1)
                return fallbacks[index]
            return getDrawable(ctx, res)
        }

        @SuppressLint("RestrictedApi")
        fun getDrawable(ctx: Context, @DrawableRes res: Int): Drawable {
            val mgr = AppCompatDrawableManager.get()
            return mgr.getDrawable(ctx, res)
        }
    }
}