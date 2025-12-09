package com.android.inventorytracker.util

fun onUnitChange(
    unit: Float,
    threshold: Int,
    onUnit: (Float) -> Unit,
    onSubUnit: (Int) -> Unit,
) {
    onUnit(unit)
    onSubUnit((unit * threshold).toInt())
}

fun onSubUnitChange(
    subUnit: Int,
    threshold: Int,
    onUnit: (Float) -> Unit,
    onSubUnit: (Int) -> Unit,
) {
    onSubUnit(subUnit)
    onUnit(subUnit.toFloat() / threshold)
}