package com.tymi.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import com.tymi.AppPreferences
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.activity.LoginActivity
import com.tymi.interfaces.IDialogThemeProvider

object DialogUtils {
    private var mDialog: Dialog? = null

    fun showAlertDialog(context: Context, message: String) {
        showAlertDialog(context, null, message, null, null, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: String, message: String) {
        showAlertDialog(context, title, message, null, null, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: String, message: String, positive: String) {
        showAlertDialog(context, title, message, positive, null, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: String, message: String, positive: String,
                        positiveRunnable: Runnable) {
        showAlertDialog(context, title, message, positive, positiveRunnable, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: String, message: String, positive: String,
                        positiveRunnable: Runnable, negative: String) {
        showAlertDialog(context, title, message, positive, positiveRunnable, null, null, negative, null)
    }

    fun showAlertDialog(context: Context, title: String, message: String, positive: String,
                        positiveRunnable: Runnable, negative: String, negativeRunnable: Runnable) {
        showAlertDialog(context, title, message, positive, positiveRunnable, null, null, negative, negativeRunnable)
    }

    fun showAlertDialog(context: Context, message: Int) {
        showAlertDialog(context, null, context.getString(message), null, null, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: Int, message: Int) {
        showAlertDialog(context, context.getString(title), context.getString(message), null, null,
                null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: Int, message: Int, positive: Int) {
        showAlertDialog(context, context.getString(title), context.getString(message),
                context.getString(positive), null, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: Int, message: Int, positive: Int,
                        positiveRunnable: Runnable) {
        showAlertDialog(context, context.getString(title), context.getString(message),
                context.getString(positive), positiveRunnable, null, null, null, null)
    }

    fun showAlertDialog(context: Context, title: Int, message: Int, positive: Int,
                        positiveRunnable: Runnable, negative: Int) {
        showAlertDialog(context, context.getString(title), context.getString(message),
                context.getString(positive), positiveRunnable, null, null, context.getString(negative), null)
    }

    fun showAlertDialog(context: Context, title: Int, message: Int, positive: Int,
                        positiveRunnable: Runnable?, negative: Int, negativeRunnable: Runnable?) {
        showAlertDialog(context, context.getString(title), context.getString(message),
                context.getString(positive), positiveRunnable, null, null, context.getString(negative), negativeRunnable)
    }

    private fun showAlertDialog(context: Context, title: String?, message: String,
                                positive: String?, positiveRunnable: Runnable?,
                                neutral: String?, neutralRunnable: Runnable?,
                                negative: String?, negativeRunnable: Runnable?) {
        val builder: AlertDialog.Builder? = if (context is IDialogThemeProvider)
            AlertDialog.Builder(context, context.getDialogTheme())
        else
            AlertDialog.Builder(context)
        builder?.setMessage(message)
        val titleStr = if (title.isNullOrEmpty()) context.getString(R.string.app_name) else title
        builder?.setTitle(titleStr)
        val positiveStr = if (positive.isNullOrEmpty()) context.getString(R.string.ok) else positive
        builder?.setPositiveButton(positiveStr, { _, _ ->
            positiveRunnable?.run()
        })

        if (!neutral.isNullOrEmpty()) {
            builder?.setNeutralButton(neutral, { _, _ ->
                neutralRunnable?.run()
            })
        }

        if (!negative.isNullOrEmpty()) {
            builder?.setNegativeButton(negative, { _, _ ->
                negativeRunnable?.run()
            })
        }
        builder?.setCancelable(false)
        builder?.create()?.show()
    }

    fun showSessionExpireDialog(context: Context) {
        showAlertDialog(context, R.string.app_name, R.string.msg_session_expire,
                R.string.ok, Runnable {
            TYMIApp.Companion.mFireBaseAuth?.signOut()
            AppPreferences.clearData()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
        })
    }

    fun showProgressDialog(context: Context) {
        mDialog = Dialog(context)
        mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog?.setContentView(R.layout.dlg_progress)
        mDialog?.setCancelable(false)
        mDialog?.show()
    }

    fun hideProgressDialog() {
        if (mDialog != null && mDialog?.isShowing!!)
            mDialog?.dismiss()
    }
}