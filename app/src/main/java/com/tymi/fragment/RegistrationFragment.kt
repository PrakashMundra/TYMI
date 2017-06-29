package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.tymi.Constants
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.entity.LookUp
import com.tymi.entity.UserProfile
import com.tymi.interfaces.IDataCallback
import com.tymi.utils.DialogUtils
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment(), View.OnClickListener, TextView.OnEditorActionListener,
        GenericTextWatcher.TextWatcherHandler {
    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_registration
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
            DialogUtils.showProgressDialog(context)
            loadDataWithoutUser(Constants.DataBase.ROLES, object : IDataCallback {
                override fun onDataCallback(user: FirebaseUser?, data: DataSnapshot) {
                    data.children.forEach { child ->
                        roles.add(child.getValue(LookUp::class.java))
                    }
                    role?.setAdapterWithDefault(roles)
                    DialogUtils.hideProgressDialog()
                }
            })
        } else
            role?.setAdapterWithDefault(roles)
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
            DialogUtils.showProgressDialog(context)
            val email = et_email?.text.toString()
            val password = et_password?.text.toString()
            val userProfile = UserProfile(et_full_name?.text.toString(),
                    role?.getSelectedItem() as LookUp,
                    et_dob.getValue())
            TYMIApp.mFireBaseAuth?.createUserWithEmailAndPassword(email, password)?.
                    addOnSuccessListener {
                        val user = TYMIApp.mFireBaseAuth?.currentUser
                        TYMIApp.mDataBase?.child(Constants.DataBase.USER_PROFILE)?.child(user?.uid)?.setValue(userProfile)?.
                                addOnSuccessListener {
                                    DialogUtils.hideProgressDialog()
                                    activity.setResult(Activity.RESULT_OK)
                                    activity.finish()
                                }?.
                                addOnFailureListener { e ->
                                    DialogUtils.hideProgressDialog()
                                    DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                                }
                    }?.
                    addOnFailureListener { e ->
                        DialogUtils.hideProgressDialog()
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }

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
            if (selectedRole.id.contentEquals(Constants.DEFAULT_LOOK_ID)) {
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