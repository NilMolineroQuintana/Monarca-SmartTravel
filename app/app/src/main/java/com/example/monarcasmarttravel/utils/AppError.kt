package com.example.monarcasmarttravel.utils

import com.example.monarcasmarttravel.R

enum class AppError(val code: Int, val stringRes: Int) {

    OK              (0,    R.string.error_ok),
    NON_EXISTING_TRIP(1,   R.string.error_non_existing_trip),
    ITEM_NOT_FOUND  (2,    R.string.error_item_not_found),
    ITEM_OUT_OF_RANGE(3,   R.string.error_item_out_of_range),
    NON_EXISTING_DATE(9998, R.string.error_non_existing_date),
    UNKNOWN         (9999, R.string.error_unknown);

    companion object {
        fun fromCode(code: Int): AppError =
            entries.find { it.code == code } ?: UNKNOWN
    }
}