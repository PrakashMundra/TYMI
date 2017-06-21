package com.tymi.interfaces

import com.tymi.entity.LookUp

interface ISpinnerWidget {
    fun onSpinnerSelection(viewId: Int, position: Int, lookUp: LookUp)
}