package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.tymi.R
import com.tymi.interfaces.ILoginActivity
import com.tymi.utils.DrawableUtils
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(), View.OnClickListener, TextView.OnEditorActionListener,
        GenericTextWatcher.TextWatcherHandler {
    private var ILoginActivity: ILoginActivity? = null

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ILoginActivity) {
            ILoginActivity = context
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        DrawableUtils.setCompoundDrawableLocale(et_email, R.drawable.ic_user_name)
        DrawableUtils.setCompoundDrawableLocale(et_password, R.drawable.ic_password)
        et_email?.addTextChangedListener(GenericTextWatcher(et_email as EditText, this))
        et_password?.addTextChangedListener(GenericTextWatcher(et_password as EditText, this))
        et_password?.setOnEditorActionListener(this)
        btn_login.setOnClickListener(this)
        btn_forgot_password.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> doLogin()
            R.id.btn_forgot_password -> doForgotPassword()
            R.id.btn_register -> ILoginActivity?.showRegistration()
        }
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            doLogin()
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
            R.id.et_password -> et_password?.isSelected = false
        }
    }

    private fun doLogin() {
        //if (validations()) {
        ILoginActivity?.showHome()
        //}
    }

    private fun validations(): Boolean {
        var isValid = true
        val email = et_email?.text
        if (email?.trim().isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email?.isSelected = true
            isValid = false
        }

        val password = et_password?.text
        if (password?.trim().isNullOrEmpty()) {
            et_password?.isSelected = true
            isValid = false
        }
        return isValid
    }

    private fun doForgotPassword() {
        val email = et_email?.text
        if (email?.trim().isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            ILoginActivity?.showForgotPassword(et_email?.text.toString())
        else
            ILoginActivity?.showForgotPassword("")
    }
}