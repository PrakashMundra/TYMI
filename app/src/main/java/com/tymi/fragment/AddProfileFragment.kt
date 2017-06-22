package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.LookUp
import com.tymi.entity.Profile
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ISaveDataCallback
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_add_profile.*

class AddProfileFragment : BaseFragment(), View.OnClickListener, GenericTextWatcher.TextWatcherHandler {
    private var mPosition = Constants.DEFAULT_POSITION

    companion object {
        fun newInstance(position: Int): AddProfileFragment {
            val fragment = AddProfileFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.Extras.POSITION, position)
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
        if (arguments != null) {
            mPosition = arguments.getInt(Constants.Extras.POSITION)
            if (mPosition != Constants.DEFAULT_POSITION)
                setProfileData()
        }
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
            loadDataWithoutUser(Constants.DataBase.ROLES, object : IDataCallback {
                override fun onDataCallback(user: FirebaseUser?, data: DataSnapshot) {
                    data.children.forEach { child ->
                        roles.add(child.getValue(LookUp::class.java))
                    }
                    role?.setAdapterWithDefault(roles)
                }
            })
        } else
            role?.setAdapterWithDefault(roles)
    }

    private fun setProfileData() {
        val profile = getDataModel().childProfiles[mPosition]
        et_full_name?.text = SpannableStringBuilder(profile.fullName)
        role?.setSelectedValue(profile.role)
        et_dob?.setValue(profile.dateOfBirth)
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
            if (mPosition != Constants.DEFAULT_POSITION) {
                val profile = getDataModel().childProfiles[mPosition]
                getDataModel().childProfiles[mPosition] = getProfile(profile.id)
                //TODO update data in FireBase
            } else {
                val key = mDataBase?.child(Constants.DataBase.CHILD_PROFILES)?.push()?.key
                val profile = getProfile(key!!)
                saveArrayData(Constants.DataBase.CHILD_PROFILES, profile, object : ISaveDataCallback {
                    override fun onSaveDataCallback(user: FirebaseUser?, isSuccess: Boolean) {
                        getDataModel().childProfiles.add(profile)
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()
                    }
                })
            }
        }
    }

    private fun getProfile(id: String): Profile {
        return Profile(id,
                et_full_name?.text.toString(),
                "",
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