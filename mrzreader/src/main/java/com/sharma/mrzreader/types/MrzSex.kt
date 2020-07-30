package com.sharma.mrzreader.types

/**
 * MRZ sex.
 */
enum class MrzSex(
        /**
         * The MRZ character.
         */
        val mrz: Char
) {

    Male('M'),
    Female('F'),
    Unspecified('X');


    companion object {

        fun fromMrz(sex: Char): MrzSex {
            when (sex) {
                'M' -> return Male
                'F' -> return Female
                '<', 'X' -> return Unspecified
                else -> throw RuntimeException("Invalid MRZ sex character: $sex")
            }
        }
    }
}
