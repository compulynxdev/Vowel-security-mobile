package com.sharma.mrzreader.types

import android.util.Log
import java.io.Serializable

/**
 * Holds a MRZ date type.
 */
class MrzDate : Serializable, Comparable<MrzDate> {

    /**
     * Year, 00-99.
     *
     *
     * Note: I am unable to find a specification of conversion of this value to a full year value.
     */
    val year: Int

    /**
     * Month, 1-12.
     */
    val month: Int

    /**
     * Day, 1-31.
     */
    val day: Int

    private val mrz: String?

    /**
     * Is the date valid or not
     */
    /**
     * Returns the date validity
     * @return Returns a boolean true if the parsed date is valid, false otherwise
     */
    val isDateValid: Boolean

    constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        isDateValid = check()
        this.mrz = null
    }

    constructor(year: Int, month: Int, day: Int, raw: String) {
        this.year = year
        this.month = month
        this.day = day
        isDateValid = check()
        this.mrz = raw
    }

    override fun toString(): String {
        return String.format("%02d/%02d/%02d", day, month, year)
        //return "$day/$month/$year"
    }

    fun toMrz(): String {
        return mrz ?: String.format("%02d%02d%02d", year, month, day)
    }

    private fun check(): Boolean {
        if (year < 0 || year > 99) {
            Log.e(TAG, "Parameter year: invalid value $year: must be 0..99")
            return false
        }
        if (month < 1 || month > 12) {
            Log.e(TAG, "Parameter month: invalid value $month: must be 1..12")
            return false
        }
        if (day < 1 || day > 31) {
            Log.e(TAG, "Parameter day: invalid value $day: must be 1..31")
            return false
        }

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val mrzDate = other as MrzDate?
        if (this.year != mrzDate!!.year) {
            return false
        }
        if (this.month != mrzDate.month) {
            return false
        }
        return this.day == mrzDate.day
    }

    override fun hashCode(): Int {
        var hash = 7
        hash = 11 * hash + this.year
        hash = 11 * hash + this.month
        hash = 11 * hash + this.day
        return hash
    }

    override fun compareTo(other: MrzDate): Int {
        return Integer.valueOf(year * 10000 + month * 100 + day)
                .compareTo(other.year * 10000 + other.month * 100 + other.day)
    }

    companion object {
        private const val serialVersionUID = 1L

        private val TAG = MrzDate::class.java.name
    }
}
