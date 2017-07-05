package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.tymi.AppPreferences
import com.tymi.Constants
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.entity.LookUp
import com.tymi.entity.Profile
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ISaveDataCallback
import com.tymi.utils.DialogUtils
import com.tymi.utils.GenericTextWatcher
import com.tymi.utils.JSonUtils
import kotlinx.android.synthetic.main.fragment_add_profile.*

class AddProfileFragment : BaseFragment(), View.OnClickListener, GenericTextWatcher.TextWatcherHandler {
    private var mPosition = Constants.DEFAULT_POSITION
    private var isEdit: Boolean = false

    companion object {
        fun newInstance(position: Int, isEdit: Boolean): AddProfileFragment {
            val fragment = AddProfileFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.Extras.POSITION, position)
            bundle.putBoolean(Constants.Extras.EDIT, isEdit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_add_profile
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        et_dob?.isMaxToday = true
        et_full_name?.addTextChangedListener(GenericTextWatcher(et_full_name as EditText, this))
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
                        roles.add(child.getValue(LookUp::class.java)!!)
                    }
                    role?.setAdapterWithDefault(roles)
                    setProfileData()
                    DialogUtils.hideProgressDialog()
                }
            })
        } else {
            role?.setAdapterWithDefault(roles)
            setProfileData()
        }
    }

    private fun setProfileData() {
        if (arguments != null) {
            mPosition = arguments.getInt(Constants.Extras.POSITION)
            isEdit = arguments.getBoolean(Constants.Extras.EDIT, false)
            if (isEdit) {
                val profile = getDataModel().profile
                et_full_name?.text = SpannableStringBuilder(profile?.fullName)
                role?.setSelectedValue(profile?.role as LookUp)
                et_dob?.setValue(profile?.dateOfBirth as String)
            } else if (mPosition != Constants.DEFAULT_POSITION) {
                val profile = getDataModel().childProfiles[mPosition]
                et_full_name?.text = SpannableStringBuilder(profile.fullName)
                role?.setSelectedValue(profile.role)
                et_dob?.setValue(profile.dateOfBirth)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> submitProfile()
            R.id.btn_cancel -> activity.finish()
        }
    }

    override fun beforeTextChanged() {

    }

    override fun onTextChanged() {

    }

    override fun afterTextChanged(view: View?) {
        when (view?.id) {
            R.id.et_full_name -> et_full_name?.isSelected = false
        }
    }

    private fun submitProfile() {
        if (validations()) {
            if (isEdit) {
                val profile = getDataModel().profile
                if (profile != null) {
                    val id = profile.id
                    val updatedProfile = getProfile(id, profile.email)
                    updateDataUserLevel(Constants.DataBase.USER_PROFILE, updatedProfile, object : ISaveDataCallback {
                        override fun onSaveDataCallback(user: FirebaseUser?) {
                            getDataModel().profile = updatedProfile
                            val profileData = JSonUtils.toJson(updatedProfile)
                            getAppPreferences().putString(AppPreferences.USER_PROFILE, profileData)
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        }
                    })
                }
            } else if (mPosition != Constants.DEFAULT_POSITION) {
                val profile = getDataModel().childProfiles[mPosition]
                val id = profile.id
                val updatedProfile = getProfile(id, "")
                updateData(Constants.DataBase.CHILD_PROFILES, id, updatedProfile, object : ISaveDataCallback {
                    override fun onSaveDataCallback(user: FirebaseUser?) {
                        getDataModel().childProfiles[mPosition] = updatedProfile
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()
                    }
                })
            } else {
                val key = TYMIApp.mDataBase?.child(Constants.DataBase.CHILD_PROFILES)?.push()?.key
                val profile = getProfile(key!!, "")
                saveArrayData(Constants.DataBase.CHILD_PROFILES, key, profile, object : ISaveDataCallback {
                    override fun onSaveDataCallback(user: FirebaseUser?) {
                        getDataModel().childProfiles.add(profile)
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()
                    }
                })
            }
        }
    }

    private fun getProfile(id: String, email: String): Profile {
        return Profile(id,
                et_full_name?.text.toString(),
                email,
                role?.getSelectedItem() as LookUp,
                et_dob?.getValue() as String)
    }

    private fun validations(): Boolean {
        var isValid = true
        val fullName = et_full_name?.text
        if (fullName?.trim().isNullOrEmpty()) {
            et_full_name?.isSelected = true
            isValid = false
        }

        val selectedRole = role?.getSelectedItem()
        if (selectedRole != null && selectedRole is LookUp) {
            if (selectedRole.id.contentEquals(Constants.DEFAULT_LOOK_ID)) {
                role?.isSelected = true
                isValid = false
            }
        }

        val dob = et_dob?.getValue()
        if (dob?.trim().isNullOrEmpty()) {
            et_dob?.isSelected = true
            isValid = false
        }
        return isValid
    }
}