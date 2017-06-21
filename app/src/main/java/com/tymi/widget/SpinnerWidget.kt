package com.tymi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.SpinnerAdapter
import com.tymi.entity.LookUp
import com.tymi.interfaces.ISpinnerWidget
import kotlinx.android.synthetic.main.widget_spinner.view.*

class SpinnerWidget : BaseWidget, AdapterView.OnItemSelectedListener {
    private var mViewId: Int = 0
    private var iSpinnerWidget: ISpinnerWidget? = null
    private var mLookUps: ArrayList<LookUp>? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayoutId(): Int {
        return R.layout.widget_spinner
    }

    override fun init() {
        super.init()
        widget_spinner?.onItemSelectedListener = this
    }

    override fun processAttributes(context: Context?, attrs: AttributeSet?, defStyle: Int?) {
        super.processAttributes(context, attrs, defStyle)
        val a = context?.obtainStyledAttributes(attrs, R.styleable.SpinnerWidget)
        if (a != null) {
            val titleStr = a.getString(R.styleable.SpinnerWidget_SpinnerWidget_key)
            widget_spinner_key?.text = titleStr
            a.recycle()
        }
    }

    fun setAdapter(lookUps: ArrayList<LookUp>) {
        this.mLookUps = lookUps
        val adapter = SpinnerAdapter(context, R.layout.list_item_spinner, mLookUps!!)
        widget_spinner?.adapter = adapter
    }

    fun setAdapterWithDefault(lookUps: ArrayList<LookUp>) {
        this.mLookUps = ArrayList<LookUp>()
        this.mLookUps?.add(LookUp(Constants.DEFAULT_ID, context.getString(R.string.select)))
        this.mLookUps?.addAll(lookUps)
        val adapter = SpinnerAdapter(context, R.layout.list_item_spinner, mLookUps!!)
        widget_spinner?.adapter = adapter
    }

    fun getSelectedItem(): Any? {
        return widget_spinner?.selectedItem
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        widget_spinner?.isSelected = selected
    }

    fun setWidgetId(viewId: Int) {
        this.mViewId = viewId
    }

    fun setCallBack(callback: ISpinnerWidget) {
        this.iSpinnerWidget = callback
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedItem = getSelectedItem()
        if (selectedItem != null && selectedItem is LookUp) {
            if (selectedItem.id != Constants.DEFAULT_ID)
                isSelected = false
            if (iSpinnerWidget != null)
                iSpinnerWidget?.onSpinnerSelection(mViewId, position, selectedItem)
        }
    }

    fun setSelectedValue(lookUpId: Int) {
        if (mLookUps?.size != 0) {
            for ((index, lookUp) in mLookUps?.withIndex()!!) {
                if (lookUp.id == lookUpId) {
                    widget_spinner?.setSelection(index)
                    break
                }
            }
        }
    }

    fun setSelectedValue(lookUp: LookUp) {
        if (mLookUps?.size != 0) {
            for ((index, slookUp) in mLookUps?.withIndex()!!) {
                if (slookUp.id == lookUp.id) {
                    widget_spinner?.setSelection(index)
                    break
                }
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        widget_spinner?.isEnabled = enabled
    }
}