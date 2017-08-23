package com.tymi.widget

import android.app.DatePickerDialog
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tymi.Constants
import com.tymi.R
import com.tymi.utils.DrawableUtils
import kotlinx.android.synthetic.main.widget_date.view.*
import java.text.SimpleDateFormat
import java.util.*

class DateViewWidget : BaseWidget, View.OnClickListener {
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    var isMaxToday = false
        set(value) {
            field = value
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun getLayoutId(): Int {
        return R.layout.widget_date
    }

    override fun init() {
        super.init()
        DrawableUtils.setCompoundDrawableLocaleInverse(widget_date_value as TextView,
                R.drawable.ic_date_picker)
        widget_date_value?.setOnClickListener(this)
    }

    override fun processAttributes(context: Context?, attrs: AttributeSet?, defStyle: Int?) {
        super.processAttributes(context, attrs, defStyle)
        val a = context?.obtainStyledAttributes(attrs, R.styleable.DateViewWidget)
        if (a != null) {
            val titleStr = a.getString(R.styleable.DateViewWidget_DateViewWidget_key)
            widget_date_key?.text = titleStr
            isMaxToday = a.getBoolean(R.styleable.DateViewWidget_DateViewWidget_max_today, false)
            a.recycle()
        }
    }

    override fun onClick(p0: View?) {
        showDialog()
    }

    private fun showDialog() {
        var isCancel = false
        val picker = DatePickerDialog(context, R.style.DialogTheme, null, mYear, mMonth, mDay)
        if (isMaxToday) {
            val today = GregorianCalendar()
            today.set(Calendar.HOUR_OF_DAY, 23)
            today.set(Calendar.MINUTE, 59)
            today.set(Calendar.SECOND, 59)
            today.set(Calendar.MILLISECOND, 999)
            picker.datePicker.maxDate = (today.timeInMillis)
        }

        picker.setOnDismissListener {
            if (!isCancel) {
                val datePicker = picker.datePicker
                if (datePicker != null) {
                    mYear = datePicker.year
                    mMonth = datePicker.month
                    mDay = datePicker.dayOfMonth
                    setDate()
                }
            }
        }
        picker.setOnCancelListener {
            isCancel = true
        }
        picker.show()
    }

    private fun setDate() {
        val calender = Calendar.getInstance()
        calender.set(mYear, mMonth, mDay)
        val date = calender.time
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)
        val dateStr = dateFormat.format(date)
        widget_date_value?.text = SpannableStringBuilder(dateStr)
        widget_date_value?.isSelected = false
    }

    fun setValue(dateStr: String) {
        if (!dateStr.isNullOrEmpty()) {
            widget_date_value?.text = SpannableStringBuilder(dateStr)
            val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)
            val date = dateFormat.parse(dateStr)
            val calendar = Calendar.getInstance()
            calendar.time = date
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDay = calendar.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun getValue(): String {
        return widget_date_value?.text.toString()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        widget_date_value?.isEnabled = enabled
    }
}