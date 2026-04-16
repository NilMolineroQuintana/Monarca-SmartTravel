package com.example.monarcasmarttravel.utils

import com.example.monarcasmarttravel.R

enum class AppError(val code: Int, val stringRes: Int) {

    OK              (0,    R.string.error_ok),
    NON_EXISTING_TRIP(1,   R.string.error_non_existing_trip),
    NON_EXISTING_ITEM  (2,    R.string.error_item_not_found),
    ITEM_OUT_OF_RANGE(3,   R.string.error_item_out_of_range),
    INVALID_TITLE   (4,    R.string.error_invalid_title),
    INVALID_DESCRIPTION(5, R.string.error_invalid_description),
    INVALID_DATE_RANGE(6,  R.string.error_invalid_date_range),
    EXISTING_EMAIL  (7,    R.string.error_existing_email),
    EXISTING_USERNAME(8,   R.string.error_existing_username),
    DIFFERENT_PASSWORDS(9, R.string.error_non_matching_passwords),
    REQUIREMENTS_PASSWORD(10, R.string.error_password_length),
    NON_EXISTING_DATE(9998, R.string.error_non_existing_date),
    UNKNOWN         (9999, R.string.error_unknown);

    companion object {
        fun fromCode(code: Int): AppError =
            entries.find { it.code == code } ?: UNKNOWN
    }
}