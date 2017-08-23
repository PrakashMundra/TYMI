package com.tymi.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.Editable
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
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
        GenericTextWatcher.TextWatcherHandler, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var mDeviceId: String = ""

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_registration
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLocation()
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null && mGoogleApiClient?.isConnected!!)
            mGoogleApiClient?.disconnect()
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
                        roles.add(child.getValue(LookUp::class.java)!!)
                    }
                    role?.setAdapterWithDefault(roles)
                    DialogUtils.hideProgressDialog()
                }
            })
        } else
            role?.setAdapterWithDefault(roles)
    }

    private fun initLocation() {
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //check whether location service is enable or not in your  phone
        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        return mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!! ||
                mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!
    }

    private fun showAlert() {
        DialogUtils.showAlertDialog(context, R.string.location, R.string.msg_location_settings, R.string.ok,
                Runnable {
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }, R.string.cancel, null)
    }

    override fun onConnected(bundle: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(result: ConnectionResult) {

    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE),
                        Constants.RequestCodes.PERMISSIONS)
            } else {
                startLocationUpdates()
                getDeviceId()
            }
        } else {
            startLocationUpdates()
            getDeviceId()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.RequestCodes.PERMISSIONS -> {
                startLocationUpdates()
                getDeviceId()
            }
        }
    }

    private fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.UPDATE_INTERVAL)
                .setFastestInterval(Constants.FASTEST_INTERVAL)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    private fun getDeviceId() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mDeviceId = if (tm.imei != null) tm.imei else ""
        else {
            @Suppress("DEPRECATION")
            mDeviceId = if (tm.deviceId != null) tm.deviceId else ""
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            mLocation = location
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        } else
            DialogUtils.showAlertDialog(context, R.string.msg_location)
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

    override fun afterTextChanged(editable: Editable?, view: View?) {
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
                    role?.getSelectedItem() as LookUp, et_dob.getValue(),
                    mLocation?.latitude.toString(), mLocation?.longitude.toString()
                    , mDeviceId)
            TYMIApp.mFireBaseAuth?.createUserWithEmailAndPassword(email, password)?.
                    addOnSuccessListener {
                        val user = TYMIApp.mFireBaseAuth?.currentUser
                        TYMIApp.mDataBase?.child(Constants.DataBase.USER_PROFILE)?.child(user?.uid)?.setValue(userProfile)?.
                                addOnSuccessListener {
                                    DialogUtils.hideProgressDialog()
                                    DialogUtils.showAlertDialog(context, R.string.registration,
                                            R.string.msg_registration, R.string.ok,
                                            Runnable {
                                                activity.setResult(Activity.RESULT_OK)
                                                activity.finish()
                                            })
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
        if (password.trim().isEmpty()) {
            et_password?.isSelected = true
            setErrorView(et_password?.parent as View)
            isValid = false
        }

        val confirmPassword = et_confirm_password?.text.toString()
        if (confirmPassword.trim().isEmpty()) {
            et_confirm_password?.isSelected = true
            setErrorView(et_confirm_password?.parent as View)
            isValid = false
        }

        if (!password.contentEquals(confirmPassword)) {
            et_confirm_password?.isSelected = true
            setErrorView(et_confirm_password?.parent as View)
            isValid = false
        }

        if (mLocation == null) {
            DialogUtils.showAlertDialog(context, R.string.msg_location)
            isValid = false
        }

        if (mDeviceId.isEmpty()) {
            DialogUtils.showAlertDialog(context, R.string.msg_device_id)
            isValid = false
        }
        return isValid
    }
}