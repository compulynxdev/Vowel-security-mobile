package com.sharma.mrzreader.types

import com.sharma.mrzreader.MrzParseException
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.records.*

/**
 * Lists all supported MRZ formats. Note that the order of the enum constants are important, see for example [.FRENCH_ID].
 */
enum class MrzFormat(
        val rows: Int,
        val columns: Int,
        private val recordClass: Class<out MrzRecord>) {

    /**
     * MRTD td1 format: A three line long, 30 characters per line format.
     */
    MRTD_TD1(3, 30, MrtdTd1::class.java),

    /**
     * French 2 line/36 characters per line format, used with French ID cards.
     * Need to occur before the [.MRTD_TD2] enum constant because of the same values for row/column.
     * See below for the "if" test.
     */
    FRENCH_ID(2, 36, FrenchIdCard::class.java) {

        override fun isFormatOf(mrzRows: Array<String>): Boolean {
            return if (!super.isFormatOf(mrzRows)) {
                false
            } else mrzRows[0].substring(0, 5) == "IDFRA"
        }
    },

    /**
     * MRV type-B format: A two lines long, 36 characters per line format.
     * Need to occur before the [.MRTD_TD2] enum constant because of the same values for row/column.
     * See below for the "if" test.
     */
    MRV_VISA_B(2, 36, MrvB::class.java) {

        override fun isFormatOf(mrzRows: Array<String>): Boolean {
            return if (!super.isFormatOf(mrzRows)) {
                false
            } else mrzRows[0].substring(0, 1) == "V"
        }
    },

    /**
     * MRTD td2 format: A two line long, 36 characters per line format.
     */
    MRTD_TD2(2, 36, MrtdTd2::class.java),

    /**
     * MRV type-A format: A two lines long, 44 characters per line format
     * Need to occur before [.PASSPORT] constant because of the same values for row/column.
     * See below for the "if" test.
     */
    MRV_VISA_A(2, 44, MrvA::class.java) {

        override fun isFormatOf(mrzRows: Array<String>): Boolean {
            return if (!super.isFormatOf(mrzRows)) {
                false
            } else mrzRows[0].substring(0, 1) == "V"
        }
    },

    /**
     * MRP Passport format: A two line long, 44 characters per line format.
     */
    PASSPORT(2, 44, MRP::class.java),

    /**
     * Unknown 2 line/34 characters per line format, used with old Slovak ID cards.
     */
    SLOVAK_ID_234(2, 34, SlovakId2_34::class.java);

    /**
     * Checks if this format is able to parse given serialized MRZ record.
     * @param mrzRows MRZ record, separated into rows.
     * @return true if given MRZ record is of this type, false otherwise.
     */
    open fun isFormatOf(mrzRows: Array<String>): Boolean {
        return rows == mrzRows.size && columns == mrzRows[0].length
    }

    /**
     * Creates new record instance with this type.
     * @return never null record instance.
     */
    fun newRecord(): MrzRecord {
        try {
            return recordClass.newInstance()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    companion object {

        /**
         * Detects given MRZ format.
         * @param mrz the MRZ string.
         * @return the format, never null.
         */
        operator fun get(mrz: String): MrzFormat {
            val rows = mrz.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val cols = rows[0].length
            for (i in 1 until rows.size) {
                if (rows[i].length != cols) {
                    throw MrzParseException(
                            "Different row lengths: 0: " + cols + " and " + i + ": " + rows[i].length,
                            mrz,
                            MrzRange(0, 0, 0),
                            null
                    )
                }
            }
            for (f in values()) {
                if (f.isFormatOf(rows)) {
                    return f
                }
            }
            throw MrzParseException(
                    "Unknown format / unsupported number of cols/rows: " + cols + "/" + rows.size,
                    mrz,
                    MrzRange(0, 0, 0),
                    null
            )
        }
    }
}
