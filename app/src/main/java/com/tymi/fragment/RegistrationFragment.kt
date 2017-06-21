package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.LookUp
import com.tymi.entity.Profile
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment(), View.OnClickListener, TextView.OnEditorActionListener,
        GenericTextWatcher.TextWatcherHandler {
    private var isEdit: Boolean = false

    companion object {
        fun newInstance(isEdit: Boolean): RegistrationFragment {
            val fragment = RegistrationFragment()
            val bundle = Bundle()
            bundle.putBoolean(Constants.Extras.EDIT, isEdit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_registration
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (arguments != null) {
            isEdit = arguments.get(Constants.Extras.EDIT) as Boolean
            if (isEdit)
                setProfile()
        }
    }

    private fun initViews() {
        mScrollView = scrollView
        et_dob?.isMaxToday = true
        et_full_name?.addTextChangedListener(GenericTextWatcher(et_full_name as EditText, this))
        et_email?.addTextChangedListener(GenericTextWatcher(et_email as EditText, this))
        et_password?.addTextChangedListener(GenericTextWatcher(et_password as EditText, this))
        et_confirm_password?.addTextChangedListener(GenericTextWatcher(et_confirm_password as EditText, this))
        et_confirm_password?.setOnEditorActionListener(this)
        btn_submit.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        setRoles()
    }

    private fun setRoles() {
        val roles = getDataModel().roles
        if (roles.size == 0) {
            //ToDO load roles from Server
            roles.add(LookUp(1, "One"))
        }
        role?.setAdapterWithDefault(roles)
    }

    private fun setProfile() {
        val profile = getDataModel().profile
        if (profile != null) {
            et_full_name?.text = SpannableStringBuilder(profile.fullName)
            et_email?.text = SpannableStringBuilder(profile.email)
            role?.setSelectedValue(profile.role)
            et_dob?.setValue(profile.dateOfBirth)
            et_password?.text = SpannableStringBuilder("")
            et_confirm_password.text = SpannableStringBuilder("")
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> doRegister()
            R.id.btn_cancel -> activity.finish()
        }
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            doRegister()
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
            R.id.et_full_name -> et_full_name?.isSelected = false
            R.id.et_email -> et_email?.isSelected = false
            R.id.et_password -> et_password?.isSelected = false
            R.id.et_confirm_password -> et_confirm_password?.isSelected = false
        }
    }

    private fun doRegister() {
        if (validations()) {
            var id = 1
            if (isEdit) {
                val profile = getDataModel().profile
                if (profile != null)
                    id = profile.id
            }
            val profile = Profile(id,
                    et_full_name?.text.toString(),
                    et_email?.text.toString(),
                    role?.getSelectedItem() as LookUp,
                    et_dob.getValue())
            getDataModel().profile = profile
            activity.setResult(Activity.RESULT_OK)
            activity.finish()
        } else
            scrollToErrorView()
    }

    private fun validations(): Boolean {
        mErrorView = null
        var isValid = true
        val fullName = et_full_name?.text
        if (fullName?.trim().isNullOrEmpty()) {
            et_full_name?.isSelected = true
            setErrorView(et_full_name?.parent as View)
            isValid = false
        }

        val email = et_email?.text
        if (email?.trim().isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email?.isSelected = true
            setErrorView(et_email?.parent as View)
            isValid = false
        }

        val selectedRole = role?.getSelectedItem()
        if (selectedRole != null && selectedRole is LookUp) {
            if (selectedRole.id == Constants.DEFAULT_ID) {
                role?.isSelected = true
                setErrorView(role as View)
                isValid = false
            }
        }

        val dob = et_dob?.getValue()
        if (dob?.trim().isNullOrEmpty()) {
            et_dob?.isSelected = true
            setErrorView(et_dob as View)
            isValid = false
        }

        val password = et_password?.text.toString()
        if (password.trim().isNullOrEmpty()) {
            et_password?.isSelected = true
            setErrorView(et_password?.parent as View)
            isValid = false
        }

        val confirmPassword = et_confirm_password?.text.toString()
        if (confirmPassword.trim().isNullOrEmpty()) {
            et_confirm_password?.isSelected = true
            setErrorView(et_confirm_password?.parent as View)
            isValid = false
        }

        if (!password.contentEquals(confirmPassword)) {
            et_confirm_password?.isSelected = true
            setErrorView(et_confirm_password?.parent as View)
            isValid = false
        }
        return isValid
    }
}