package com.sharma.mrzreader

import java.io.Serializable

/**
 * Represents a text selection range.
 */
class MrzRange
/**
 * Creates new MRZ range object.
 * @param column 0-based index of first character in the range.
 * @param columnTo 0-based index of a character after last character in the range.
 * @param row 0-based row.
 */
(
        /**
         * 0-based index of first character in the range.
         */
        val column: Int,
        /**
         * 0-based index of a character after last character in the range.
         */
        val columnTo: Int,
        /**
         * 0-based row.
         */
        val row: Int
) : Serializable {

    init {
        if (column > columnTo) {
            throw IllegalArgumentException("Parameter column: invalid value $column: must be less than $columnTo")
        }
    }

    override fun toString(): String {
        return "$column-$columnTo,$row"
    }

    /**
     * Returns length of this range.
     * @return number of characters, which this range covers.
     */
    fun length(): Int {
        return columnTo - column
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
