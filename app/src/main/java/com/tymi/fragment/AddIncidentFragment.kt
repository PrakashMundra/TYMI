package com.tymi.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.SpannableStringBuilder
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
import com.tymi.entity.Incident
import com.tymi.entity.LookUp
import com.tymi.entity.Profile
import com.tymi.enums.IncidentStatus
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ISaveDataCallback
import com.tymi.interfaces.ISpinnerWidget
import com.tymi.utils.DateUtils
import com.tymi.utils.DialogUtils
import com.tymi.utils.GenericTextWatcher
import com.tymi.utils.NumberUtils
import kotlinx.android.synthetic.main.fragment_add_incident.*


class AddIncidentFragment : BaseFragment(), View.OnClickListener,
        GenericTextWatcher.TextWatcherHandler, TextView.OnEditorActionListener, ISpinnerWidget {
    private var mId: String? = null
    private var isEdit: Boolean = false
    private var mExpensesWatch: GenericTextWatcher? = null
    private val LOOKUPS = arrayOf(Constants.DataBase.CHILD_PROFILES, Constants.DataBase.INCIDENTS)

    companion object {
        fun newInstance(id: String, isEdit: Boolean): AddIncidentFragment {
            val fragment = AddIncidentFragment()
            val bundle = Bundle()
            bundle.putString(Constants.Extras.ID, id)
            bundle.putBoolean(Constants.Extras.EDIT, isEdit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_add_incident
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mScrollView = scrollView
        status?.setWidgetId(R.id.status)
        status?.setCallBack(this)
        start_date?.isMaxToday = true
        cause?.addTextChangedListener(GenericTextWatcher(cause as EditText, this))
        medication?.addTextChangedListener(GenericTextWatcher(medication as EditText, this))
        notes?.addTextChangedListener(GenericTextWatcher(notes as EditText, this))
        mExpensesWatch = GenericTextWatcher(expenses as EditText, this)
        expenses?.addTextChangedListener(mExpensesWatch)
        btn_submit.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        setViewsData()
    }

    private fun setViewsData() {
        setStatuses()
        DialogUtils.showProgressDialog(context)
        loadLookUp(0)
    }

    private fun setStatuses() {
        val statuses = IncidentStatus.getStatusLookUp(context)
        status?.setAdapter(statuses)
    }

    private fun loadLookUp(position: Int) {
        if (position < LOOKUPS.size) {
            val child = LOOKUPS[position]
            when (child) {
                Constants.DataBase.CHILD_PROFILES -> {
                    val childProfiles = getDataModel().childProfiles
                    if (childProfiles.size == 0) {
                        loadData(Constants.DataBase.CHILD_PROFILES, object : IDataCallback {
                            override fun onDataCallback(user: FirebaseUser?, data: DataSnapshot) {
                                data.children.forEach { child ->
                                    val childProfile = child.getValue(Profile::class.java)
                                    childProfiles.add(childProfile!!)
                                }
                                select_profile?.setAdapterWithDefault(getDataModel().profileLookUps)
                                loadLookUp(position + 1)
                            }
                        })
                    } else {
                        select_profile?.setAdapterWithDefault(getDataModel().profileLookUps)
                        loadLookUp(position + 1)
                    }
                }

                Constants.DataBase.INCIDENTS -> {
                    val incidents = getDataModel().incidentLookUps
                    if (incidents.size == 0) {
                        loadDataWithoutUser(Constants.DataBase.INCIDENTS, object : IDataCallback {
                            override fun onDataCallback(user: FirebaseUser?, data: DataSnapshot) {
                                data.children.forEach { child ->
                                    incidents.add(child.getValue(LookUp::class.java)!!)
                                }
                                select_incident?.setAdapterWithDefault(incidents)
                                loadLookUp(position + 1)
                            }
                        })
                    } else {
                        select_incident?.setAdapterWithDefault(incidents)
                        loadLookUp(position + 1)
                    }
                }
            }
        } else {
            val bundle = arguments
            if (bundle != null) {
                mId = bundle.getString(Constants.Extras.ID)
                isEdit = bundle.get(Constants.Extras.EDIT) as Boolean
                if (!mId.isNullOrEmpty())
                    setIncidentData()
            }
            DialogUtils.hideProgressDialog()
        }
    }

    private fun setIncidentData() {
        val incident = getDataModel().getIncident(mId!!)
        status?.setSelectedValue(incident.statusId)
        select_profile?.setSelectedValue(incident.profile)
        select_incident?.setSelectedValue(incident.incident)
        cause?.text = SpannableStringBuilder(incident.cause)
        start_date?.setValue(incident.startDate)
        medication?.text = SpannableStringBuilder(incident.medication)
        notes?.text = SpannableStringBuilder(incident.notes)
        end_date?.setValue(incident.endDate)
        expenses?.text = SpannableStringBuilder(incident.expenses)
        hospital?.text = SpannableStringBuilder(incident.hospital)
        if (!isEdit) {
            disableAllFields(add_incident)
            add_incident_buttons?.visibility = View.GONE
        }
        Handler().postDelayed({
            if (!isEdit)
                end_date?.isEnabled = false
        }, 1000)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> submitIncident()
            R.id.btn_cancel -> activity?.finish()
        }
    }

    override fun onSpinnerSelection(viewId: Int, position: Int, lookUp: LookUp) {
        when (viewId) {
            R.id.status -> {
                val isClosed = (lookUp.id == Constants.STATUS_CLOSE)
                end_date?.isEnabled = isClosed
                end_date?.isSelected = false
            }
        }
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            submitIncident()
            return true
        }
        return false
    }

    override fun beforeTextChanged() {

    }

    override fun onTextChanged() {

    }

    override fun afterTextChanged(editable: Editable?, view: View?) {
        when (view?.id) {
            R.id.cause -> cause?.isSelected = false
            R.id.medication -> medication?.isSelected = false
            R.id.notes -> notes?.isSelected = false
            R.id.expenses -> {
                expenses?.removeTextChangedListener(mExpensesWatch)
                expenses?.isSelected = false
                var text = editable.toString()
                text = text.replace(",", "")
                val formattedText = SpannableStringBuilder(NumberUtils.getFormattedNumber(text))
                expenses?.text = formattedText
                expenses?.setSelection(formattedText.length)
                expenses?.addTextChangedListener(mExpensesWatch)
            }
        }
    }

    private fun submitIncident() {
        if (validations()) {
            if (!mId.isNullOrEmpty()) {
                val updatedIncident = getIncident(mId!!)
                updateData(Constants.DataBase.INCIDENT_REPORTS, mId!!, updatedIncident, object : ISaveDataCallback {
                    override fun onSaveDataCallback(user: FirebaseUser?) {
                        getDataModel().updateIncident(updatedIncident);
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
                })
            } else {
                val key = TYMIApp.mDataBase?.child(Constants.DataBase.INCIDENT_REPORTS)?.push()?.key
                val incident = getIncident(key!!)
                saveArrayData(Constants.DataBase.INCIDENT_REPORTS, key, incident, object : ISaveDataCallback {
                    override fun onSaveDataCallback(user: FirebaseUser?) {
                        getDataModel().incidents.add(incident)
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
                })
            }
        } else
            scrollToErrorView()
    }

    private fun getIncident(id: String): Incident {
        val selectedStatus = status?.getSelectedItem() as LookUp
        var endDate = ""
        if (selectedStatus.id == Constants.STATUS_CLOSE)
            endDate = end_date.getValue()
        return Incident(id,
                selectedStatus.id,
                select_profile?.getSelectedItem() as LookUp,
                select_incident?.getSelectedItem() as LookUp,
                cause?.text.toString(),
                start_date.getValue(),
                medication?.text.toString(),
                notes?.text.toString(),
                endDate,
                expenses?.text.toString().replace(",", ""),
                hospital?.text.toString())
    }

    private fun validations(): Boolean {
        mErrorView = null
        var isValid = true
        val selectedStatus = status?.getSelectedItem() as LookUp
        if (selectedStatus.id.contentEquals(Constants.DEFAULT_LOOK_ID)) {
            status?.isSelected = true
            setErrorView(status as View)
            isValid = false
        }

        val profile = select_profile?.getSelectedItem() as LookUp
        if (profile.id.contentEquals(Constants.DEFAULT_LOOK_ID)) {
            select_profile?.isSelected = true
            setErrorView(select_profile as View)
            isValid = false
        }

        val incident = select_incident?.getSelectedItem() as LookUp
        if (incident.id.contentEquals(Constants.DEFAULT_LOOK_ID)) {
            select_incident?.isSelected = true
            setErrorView(select_incident as View)
            isValid = false
        }

        val causeStr = cause?.text.toString()
        if (causeStr.trim().isNullOrEmpty()) {
            cause?.isSelected = true
            setErrorView(cause?.parent as View)
            isValid = false
        }

        val startDate = start_date?.getValue() as String
        if (startDate.trim().isNullOrEmpty()) {
            start_date?.isSelected = true
            setErrorView(start_date as View)
            isValid = false
        }

        val medicationStr = medication?.text.toString()
        if (medicationStr.trim().isNullOrEmpty()) {
            medication?.isSelected = true
            setErrorView(medication?.parent as View)
            isValid = false
        }

        /*val notesStr = notes?.text.toString()
        if (notesStr.trim().isNullOrEmpty()) {
            notes?.isSelected = true
            setErrorView(notes?.parent as View)
            isValid = false
        }*/

        if (selectedStatus.id == Constants.STATUS_CLOSE) {
            val endDate = end_date?.getValue() as String
            if (endDate.trim().isNullOrEmpty()) {
                end_date?.isSelected = true
                setErrorView(end_date as View)
                isValid = false
            }

            if (DateUtils.compareDates(startDate, endDate)) {
                end_date?.isSelected = true
                setErrorView(end_date as View)
                isValid = false
            }
        }

        /*val expensesStr = expenses?.text.toString()
        if (expensesStr.trim().isNullOrEmpty()) {
            expenses?.isSelected = true
            setErrorView(expenses?.parent as View)
            isValid = false
        }*/
        return isValid
    }
}