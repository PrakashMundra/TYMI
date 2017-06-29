package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.tymi.AppPreferences
import com.tymi.Constants
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.entity.Profile
import com.tymi.entity.UserProfile
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ILoginActivity
import com.tymi.utils.DialogUtils
import com.tymi.utils.DrawableUtils
import com.tymi.utils.GenericTextWatcher
import com.tymi.utils.JSonUtils
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment(), View.OnClickListener, TextView.OnEditorActionListener,
        GenericTextWatcher.TextWatcherHandler {
    private var iLoginActivity: ILoginActivity? = null

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ILoginActivity) {
            iLoginActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        iLoginActivity = null
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

    fun clearFields() {
        et_email?.text = SpannableStringBuilder("")
        et_password?.text = SpannableStringBuilder("")
        et_email?.isSelected = false
        et_password?.isSelected = false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> doLogin()
            R.id.btn_forgot_password -> doForgotPassword()
            R.id.btn_register -> iLoginActivity?.showRegistration()
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
        if (validations()) {
            DialogUtils.showProgressDialog(context)
            val email = et_email?.text.toString()
            val password = et_password?.text.toString()
            TYMIApp.mFireBaseAuth?.signInWithEmailAndPassword(email, password)?.
                    addOnSuccessListener {
                        loadData(Constants.DataBase.USER_PROFILE, object : IDataCallback {
                            override fun onDataCallback(user: FirebaseUser?, data: DataSnapshot) {
                                val userProfile = data.getValue(UserProfile::class.java)
                                val profile = Profile(user?.uid!!,
                                        userProfile.fullName,
                                        user.email!!,
                                        userProfile.role,
                                        userProfile.dateOfBirth
                                )
                                getDataModel().profile = profile
                                val profileData = JSonUtils.toJson(profile)
                                getAppPreferences().putString(AppPreferences.USER_PROFILE, profileData)
                                DialogUtils.hideProgressDialog()
                                iLoginActivity?.showHome()
                            }
                        })
                    }?.
                    addOnFailureListener { e ->
                        et_email?.isSelected = true
                        et_password?.isSelected = true
                        DialogUtils.hideProgressDialog()
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }
        }
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
        if (!email?.trim().isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            iLoginActivity?.showForgotPassword(et_email?.text.toString())
        else
            iLoginActivity?.showForgotPassword("")
    }
}