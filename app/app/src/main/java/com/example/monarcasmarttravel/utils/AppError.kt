package com.example.monarcasmarttravel.utils

enum class AppError(val code: Int, val message: String) {

    OK (0, "Ok"),
    NON_EXISTING_TRIP (1, "El viatje no existèix."),
    ITEM_OUT_OF_RANGE (2, "L'item que has intentat crear està fora del rang del viatge."),

    NON_EXISTING_DATE(9998,"La data no està assignada."),
    UNKNOWN (9999, "S'ha produït un error inesperat.");

    companion object {
        fun fromCode(code: Int): AppError =
            entries.find { it.code == code } ?: UNKNOWN
    }
}