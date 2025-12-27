package com.android.inventorytracker.util

import kotlin.math.roundToInt

fun onUnitChange(
    unit: Float,
    threshold: Int,
    onSubUnit: (Int) -> Unit,
) {
    onSubUnit((unit * threshold).roundToInt())
}

fun onSubUnitChange(
    subUnit: Int,
    threshold: Int,
    onUnit: (Float) -> Unit,
) {
    onUnit(subUnit.toFloat() / threshold.toFloat())
}
