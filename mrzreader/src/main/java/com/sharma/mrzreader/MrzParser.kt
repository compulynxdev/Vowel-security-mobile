package com.sharma.mrzreader

import android.util.Log
import com.sharma.mrzreader.types.MrzDate
import com.sharma.mrzreader.types.MrzFormat
import com.sharma.mrzreader.types.MrzSex
import java.text.Normalizer
import java.util.*

/**
 * Parses the MRZ records.
 *
 *
 * All parse methods throws [MrzParseException] unless stated otherwise.
 */
class MrzParser
/**
 * Creates new parser which parses given MRZ record.
 * @param mrz the MRZ record, not null.
 */
(
        /**
         * The MRZ record, not null.
         */
        val mrz: String
) {
    /**
     * The MRZ record separated into rows.
     */
    val rows: Array<String>

    /**
     * MRZ record format.
     */
    val format: MrzFormat

    init {
        this.rows = mrz.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.format = MrzFormat.get(mrz)
    }

    /**
     * Parses the MRZ name in form of SURNAME<<FIRSTNAME></FIRSTNAME><
     * @Param range the range
     * @return array of [surname, first_name], never null, always with a length of 2.
     */
    fun parseName(range: MrzRange): Array<String> {
        checkValidCharacters(range)
        var str = rawValue(range)
        while (str.endsWith("<")) {
            str = str.substring(0, str.length - 1)
        }
        val names = str.split("<<".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var surname: String
        var givenNames = ""
        surname = parseString(MrzRange(range.column, range.column + names[0].length, range.row))
        if (names.size == 1) {
            givenNames =
                    parseString(MrzRange(range.column, range.column + names[0].length, range.row))
            surname = ""
        } else if (names.size > 1) {
            surname = parseString(MrzRange(range.column, range.column + names[0].length, range.row))
            givenNames = parseString(
                    MrzRange(
                            range.column + names[0].length + 2,
                            range.column + str.length,
                            range.row
                    )
            )
        }
        return arrayOf(surname, givenNames)
    }

    /**
     * Returns a raw MRZ value from given range. If multiple ranges are specified, the value is concatenated.
     * @param range the ranges, not null.
     * @return raw value, never null, may be empty.
     */
    fun rawValue(vararg range: MrzRange): String {
        val sb = StringBuilder()
        for (r in range) {
            sb.append(rows[r.row].substring(r.column, r.columnTo))
        }
        return sb.toString()
    }

    /**
     * Checks that given range contains valid characters.
     * @param range the range to check.
     */
    fun checkValidCharacters(range: MrzRange) {
        val str = rawValue(range)
        for (i in 0 until str.length) {
            val c = str[i]
            if (c != FILLER && (c < '0' || c > '9') && (c < 'A' || c > 'Z')) {
                throw MrzParseException(
                        "Invalid character in MRZ record: $c",
                        mrz,
                        MrzRange(range.column + i, range.column + i + 1, range.row),
                        format
                )
            }
        }
    }

    /**
     * Parses a string in given range. &lt;&lt; are replaced with ", ", &lt; is replaced by space.
     * @param range the range
     * @return parsed string.
     */
    fun parseString(range: MrzRange): String {
        checkValidCharacters(range)
        var str = rawValue(range)
        while (str.endsWith("<")) {
            str = str.substring(0, str.length - 1)
        }
        return str.replace("" + FILLER + FILLER, ", ").replace(FILLER, ' ')
    }

    /**
     * Verifies the check digit.
     * @param col the 0-based column of the check digit.
     * @param row the 0-based column of the check digit.
     * @param strRange the range for which the check digit is computed.
     * @param fieldName (optional) field name. Used only when validity check fails.
     * @return true if check digit is valid, false if not
     */
    fun checkDigit(col: Int, row: Int, strRange: MrzRange, fieldName: String): Boolean {
        return checkDigit(col, row, rawValue(strRange), fieldName)
    }

    /**
     * Verifies the check digit.
     * @param col the 0-based column of the check digit.
     * @param row the 0-based column of the check digit.
     * @param str the raw MRZ substring.
     * @param fieldName (optional) field name. Used only when validity check fails.
     * @return true if check digit is valid, false if not
     */
    fun checkDigit(col: Int, row: Int, str: String, fieldName: String): Boolean {

        /**
         * If the check digit validation fails, this will contain the location.
         */
        var invalidCheckdigit: MrzRange? = null

        val digit = (computeCheckDigit(str) + '0'.toInt()).toChar()
        var checkDigit = rows[row][col]
        if (checkDigit == FILLER) {
            checkDigit = '0'
        }
        if (digit != checkDigit) {
            invalidCheckdigit = MrzRange(col, col + 1, row)
            println("Check digit verification failed for $fieldName: expected $digit but got $checkDigit")
        }
        return invalidCheckdigit == null
    }

    /**
     * Parses MRZ date.
     * @param range the range containing the date, in the YYMMDD format. The range must be 6 characters long.
     * @return parsed date
     * @throws IllegalArgumentException if the range is not 6 characters long.
     */
    fun parseDate(range: MrzRange): MrzDate {
        if (range.length() != 6) {
            throw IllegalArgumentException("Parameter range: invalid value $range: must be 6 characters long")
        }
        var r: MrzRange
        r = MrzRange(range.column, range.column + 2, range.row)
        var year: Int
        try {
            year = Integer.parseInt(rawValue(r))
        } catch (ex: NumberFormatException) {
            year = -1
            Log.e(
                    TAG,
                    "Failed to parse MRZ date year " + rawValue(range) + ": " + ex + " " + mrz + " " + r
            )
        }

        if (year < 0 || year > 99) {
            Log.e(TAG, "Invalid year value $year: must be 0..99")
        }
        r = MrzRange(range.column + 2, range.column + 4, range.row)
        var month: Int
        try {
            month = Integer.parseInt(rawValue(r))
        } catch (ex: NumberFormatException) {
            month = -1
            Log.e(
                    TAG,
                    "Failed to parse MRZ date month " + rawValue(range) + ": " + ex + " " + mrz + " " + r
            )
        }

        if (month < 1 || month > 12) {
            Log.e(TAG, "Invalid month value $month: must be 1..12")
        }
        r = MrzRange(range.column + 4, range.column + 6, range.row)
        var day: Int
        try {
            day = Integer.parseInt(rawValue(r))
        } catch (ex: NumberFormatException) {
            day = -1
            Log.e(
                    TAG,
                    "Failed to parse MRZ date month " + rawValue(range) + ": " + ex + " " + mrz + " " + r
            )
        }

        if (day < 1 || day > 31) {
            Log.e(TAG, "Invalid day value $day: must be 1..31")
        }
        return MrzDate(year, month, day, rawValue(range))

    }

    /**
     * Parses the "sex" value from given column/row.
     * @param col the 0-based column
     * @param row the 0-based row
     * @return sex, never null.
     */
    fun parseSex(col: Int, row: Int): MrzSex {
        return MrzSex.fromMrz(rows[row][col])
    }

    companion object {

        private val TAG = MrzParser::class.java.name
        private val MRZ_WEIGHTS = intArrayOf(7, 3, 1)

        /**
         * Checks if given character is valid in MRZ.
         * @param c the character.
         * @return true if the character is valid, false otherwise.
         */
        private fun isValid(c: Char): Boolean {
            return c == FILLER || c >= '0' && c <= '9' || c >= 'A' && c <= 'Z'
        }

        private fun getCharacterValue(c: Char): Int {
            if (c == FILLER) {
                return 0
            }
            if (c >= '0' && c <= '9') {
                return c - '0'
            }
            if (c >= 'A' && c <= 'Z') {
                return c - 'A' + 10
            }
            throw RuntimeException("Invalid character in MRZ record: $c")
        }

        /**
         * Computes MRZ check digit for given string of characters.
         * @param str the string
         * @return check digit in range of 0..9, inclusive. See [MRTD documentation](http://www2.icao.int/en/MRTD/Downloads/Doc%209303/Doc%209303%20English/Doc%209303%20Part%203%20Vol%201.pdf) part 15 for details.
         */
        fun computeCheckDigit(str: String): Int {
            var result = 0
            for (i in 0 until str.length) {
                result += getCharacterValue(str[i]) * MRZ_WEIGHTS[i % MRZ_WEIGHTS.size]
            }
            return result % 10
        }

        /**
         * Computes MRZ check digit for given string of characters.
         * @param str the string
         * @return check digit in range of 0..9, inclusive. See [MRTD documentation](http://www2.icao.int/en/MRTD/Downloads/Doc%209303/Doc%209303%20English/Doc%209303%20Part%203%20Vol%201.pdf) part 15 for details.
         */
        fun computeCheckDigitChar(str: String): Char {
            return ('0'.toInt() + computeCheckDigit(str)).toChar()
        }

        /**
         * Factory method, which parses the MRZ and returns appropriate record class.
         * @param mrz MRZ to parse.
         * @return record class.
         */
        fun parse(mrz: String): MrzRecord {
            val result = MrzFormat.get(mrz).newRecord()
            result.fromMrz(mrz)
            return result
        }


        private val EXPAND_CHARACTERS = HashMap<String, String>()

        init {
            EXPAND_CHARACTERS["\u00C4"] = "AE" // Ä
            EXPAND_CHARACTERS["\u00E4"] = "AE" // ä
            EXPAND_CHARACTERS["\u00C5"] = "AA" // Å
            EXPAND_CHARACTERS["\u00E5"] = "AA" // å
            EXPAND_CHARACTERS["\u00C6"] = "AE" // Æ
            EXPAND_CHARACTERS["\u00E6"] = "AE" // æ
            EXPAND_CHARACTERS["\u0132"] = "IJ" // Ĳ
            EXPAND_CHARACTERS["\u0133"] = "IJ" // ĳ
            EXPAND_CHARACTERS["\u00D6"] = "OE" // Ö
            EXPAND_CHARACTERS["\u00F6"] = "OE" // ö
            EXPAND_CHARACTERS["\u00D8"] = "OE" // Ø
            EXPAND_CHARACTERS["\u00F8"] = "OE" // ø
            EXPAND_CHARACTERS["\u00DC"] = "UE" // Ü
            EXPAND_CHARACTERS["\u00FC"] = "UE" // ü
            EXPAND_CHARACTERS["\u00DF"] = "SS" // ß
        }

        /**
         * Converts given string to a MRZ string: removes all accents, converts the string to upper-case and replaces all spaces and invalid characters with '&lt;'.
         *
         *
         * Several characters are expanded:
         * <table border="1">
         * <tr><th>Character</th><th>Expand to</th></tr>
         * <tr><td>Ä</td><td>AE</td></tr>
         * <tr><td>Å</td><td>AA</td></tr>
         * <tr><td>Æ</td><td>AE</td></tr>
         * <tr><td>Ĳ</td><td>IJ</td></tr>
         * <tr><td>IJ</td><td>IJ</td></tr>
         * <tr><td>Ö</td><td>OE</td></tr>
         * <tr><td>Ø</td><td>OE</td></tr>
         * <tr><td>Ü</td><td>UE</td></tr>
         * <tr><td>ß</td><td>SS</td></tr>
        </table> *
         *
         *
         * Examples:
         *  * `toMrz("Sedím na konári", 20)` yields `"SEDIM<NA<KONARI<<<<<"`
         *  * `toMrz("Pat, Mat", 8)` yields `"PAT<<MAT"`
         *  * `toMrz("foo/bar baz", 4)` yields `"FOO<"`
         *  * `toMrz("*$()&/\", 8)` yields `"<<<<<<<<"`
         *
         * @param string the string to convert. Passing null is the same as passing in an empty string.
         * @param length required length of the string. If given string is longer, it is truncated. If given string is shorter than given length, '&lt;' characters are appended at the end. If -1, the string is neither truncated nor enlarged.
         * @return MRZ-valid string.
         */
        fun toMrz(string: String?, length: Int): String {
            var tmpStr = string
            if (tmpStr == null) {
                tmpStr = ""
            }
            for ((key, value) in EXPAND_CHARACTERS) {
                tmpStr = tmpStr!!.replace(key, value)
            }
            tmpStr = tmpStr!!.replace("’", "")
            tmpStr = tmpStr.replace("'", "")
            tmpStr = deaccent(tmpStr).toUpperCase(Locale.getDefault())
            if (length >= 0 && tmpStr.length > length) {
                tmpStr = tmpStr.substring(0, length)
            }
            val sb = StringBuilder(tmpStr)
            for (i in 0 until sb.length) {
                if (!isValid(sb[i])) {
                    sb.setCharAt(i, FILLER)
                }
            }
            while (sb.length < length) {
                sb.append(FILLER)
            }
            return sb.toString()
        }

        private fun isBlank(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.length == 0
        }

        /**
         * Converts a surname and given names to a MRZ string, shortening them as per Doc 9303 Part 3 Vol 1 Section 6.7 of the MRZ specification when necessary.
         * @param surName the surname, not blank.
         * @param firstName given names, not blank.
         * @param length required length of the string. If given string is longer, it is shortened. If given string is shorter than given length, '&lt;' characters are appended at the end.
         * @return name, properly converted to MRZ format of SURNAME&lt;&lt;GIVENNAMES&lt;..., with the exact length of given length.
         */
        fun nameToMrz(surName: String, firstName: String, length: Int): String {
            var surname = surName
            var givenNames = firstName
            if (isBlank(surname)) {
                throw IllegalArgumentException("Parameter surname: invalid value $surname: blank")
            }
            if (isBlank(givenNames)) {
                throw IllegalArgumentException("Parameter givenNames: invalid value $givenNames: blank")
            }
            if (length <= 0) {
                throw IllegalArgumentException("Parameter length: invalid value $length: not positive")
            }
            surname = surname.replace(", ", " ")
            givenNames = givenNames.replace(", ", " ")
            //.split("[ \n\t\f\r]+".toRegex())
            val surnames = surname.trim { it <= ' ' }.split("[ \n\t\r]+".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val given = givenNames.trim { it <= ' ' }.split("[ \n\t\r]+".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            for (i in surnames.indices) {
                surnames[i] = toMrz(surnames[i], -1)
            }
            for (i in given.indices) {
                given[i] = toMrz(given[i], -1)
            }
            // truncate
            var nameSize = getNameSize(surnames, given)
            var currentlyTruncating = given
            var currentlyTruncatingIndex = given.size - 1
            while (nameSize > length) {
                val ct = currentlyTruncating[currentlyTruncatingIndex]
                val ctsize = ct.length
                if (nameSize - ctsize + 1 <= length) {
                    currentlyTruncating[currentlyTruncatingIndex] =
                            ct.substring(0, ctsize - (nameSize - length))
                } else {
                    currentlyTruncating[currentlyTruncatingIndex] = ct.substring(0, 1)
                    currentlyTruncatingIndex--
                    if (currentlyTruncatingIndex < 0) {
                        if (currentlyTruncating.contentEquals(surnames)) {
                            throw IllegalArgumentException(
                                    "Cannot truncate name " + surname + " " + givenNames + ": length too small: " + length + "; truncated to " + toName(
                                            surnames,
                                            given
                                    )
                            )
                        }
                        currentlyTruncating = surnames
                        currentlyTruncatingIndex = currentlyTruncating.size - 1
                    }
                }
                nameSize = getNameSize(surnames, given)
            }
            return toMrz(toName(surnames, given), length)
        }

        /**
         * The filler character, '&lt;'.
         */
        val FILLER = '<'

        private fun toName(surnames: Array<String>, given: Array<String>): String {
            val sb = StringBuilder()
            var first = true
            for (s in surnames) {
                if (first) {
                    first = false
                } else {
                    sb.append(FILLER)
                }
                sb.append(s)
            }
            sb.append(FILLER)
            for (s in given) {
                sb.append(FILLER)
                sb.append(s)
            }
            return sb.toString()
        }

        private fun getNameSize(surnames: Array<String>, given: Array<String>): Int {
            var result = 0
            for (s in surnames) {
                result += s.length + 1
            }
            for (s in given) {
                result += s.length + 1
            }
            return result
        }

        private fun deaccent(str: String): String {
            val n = Normalizer.normalize(str, Normalizer.Form.NFD)
            return n.replace("[^\\p{ASCII}]".toRegex(), "").toLowerCase(Locale.getDefault())
        }
    }
}
