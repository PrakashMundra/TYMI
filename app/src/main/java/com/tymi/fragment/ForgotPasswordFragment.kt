package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.tymi.Constants
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.utils.DialogUtils
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment(), View.OnClickListener, TextView.OnEditorActionListener,
        GenericTextWatcher.TextWatcherHandler {

    companion object {
        fun newInstance(email: String): ForgotPasswordFragment {
            val fragment = ForgotPasswordFragment()
            val bundle = Bundle()
            bundle.putString(Constants.Extras.EMAIL, email)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null)
            et_email?.text = SpannableStringBuilder(arguments.getString(Constants.Extras.EMAIL))
        btn_cancel?.setOnClickListener(this)
        btn_send?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_cancel -> activity.finish()
            R.id.btn_send -> sendResetPassword()
        }
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendResetPassword()
            return true
        }
        return false
    }

    override fun beforeTextChanged() {

    }

    override fun onTextChanged() {

    }

    override fun afterTextChanged(view: View?) {
        when (view?.id) {
            R.id.et_email -> et_email?.isSelected = false
        }
    }

    private fun sendResetPassword() {
        val email = et_email?.text.toString().trim()
        if (email.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            et_email?.isSelected = true
        else {
            DialogUtils.showProgressDialog(context)
            et_email?.isSelected = false
            TYMIApp.mFireBaseAuth?.sendPasswordResetEmail(email)?.
                    addOnSuccessListener {
                        DialogUtils.hideProgressDialog()
                        DialogUtils.showAlertDialog(context, R.string.app_name, R.string.msg_forgot_password,
                                R.string.ok, Runnable {
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        })
                    }?.
                    addOnFailureListener { e ->
                        DialogUtils.hideProgressDialog()
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }
        }
    }
}