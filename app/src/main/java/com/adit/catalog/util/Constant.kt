package com.adit.catalog.util

import java.text.NumberFormat
import java.util.Locale

object Constant {
    fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    enum class SortType {
        AZ,
        ZA,
        PRICE_LOW_TO_HIGH,
        PRICE_HIGH_TO_LOW
    }

}