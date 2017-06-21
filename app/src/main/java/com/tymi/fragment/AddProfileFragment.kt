package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.LookUp
import com.tymi.entity.Profile
import com.tymi.utils.GenericTextWatcher
import kotlinx.android.synthetic.main.fragment_add_profile.*

class AddProfileFragment : BaseFragment(), View.OnClickListener, GenericTextWatcher.TextWatcherHandler {
    private var mPosition = Constants.DEFAULT_ID

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
            mPosition = arguments.get(Constants.Extras.POSITION) as Int
            if (mPosition != Constants.DEFAULT_ID)
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
            //ToDO load lookups from Server
            roles.add(LookUp(1, "One"))
        }
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
            if (mPosition != Constants.DEFAULT_ID) {
                val profile = getDataModel().childProfiles[mPosition]
                getDataModel().childProfiles[mPosition] = getProfile(profile.id)
            } else {
                val id = getDataModel().childProfiles.size + 1
                getDataModel().childProfiles.add(getProfile(id))
            }
            activity.setResult(Activity.RESULT_OK)
            activity.finish()
        }
    }

    private fun getProfile(id: Int): Profile {
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
            if (selectedRole.id == Constants.DEFAULT_ID) {
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