package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzDocumentCode
import com.sharma.mrzreader.types.MrzFormat

/**
 * MRV type-B format: A two lines long, 36 characters per line format
 */
class MrvB : MrzRecord(MrzFormat.MRV_VISA_B) {
    init {
        code1 = 'V'
        code2 = '<'
        code = MrzDocumentCode.TypeV
    }

    /**
     * Optional data at the discretion of the issuing State
     */
    //    public String optional;

    override fun fromMrz(mrz: String) {
        super.fromMrz(mrz)
        val parser = MrzParser(mrz)
        setName(parser.parseName(MrzRange(5, 36, 0)))
        documentNumber = parser.parseString(MrzRange(0, 9, 1))
        validDocumentNumber = parser.checkDigit(9, 1, MrzRange(0, 9, 1), "passport number")
        nationality = parser.parseString(MrzRange(10, 13, 1))
        dateOfBirth = parser.parseDate(MrzRange(13, 19, 1))
        validDateOfBirth = parser.checkDigit(
                19,
                1,
                MrzRange(13, 19, 1),
                "date of birth"
        ) && dateOfBirth!!.isDateValid
        sex = parser.parseSex(20, 1)
        expirationDate = parser.parseDate(MrzRange(21, 27, 1))
        validExpirationDate = parser.checkDigit(
                27,
                1,
                MrzRange(21, 27, 1),
                "expiration date"
        ) && expirationDate!!.isDateValid
        optional = parser.parseString(MrzRange(28, 36, 1))
        // TODO validComposite missing? (full MRZ line)
    }

    override fun toString(): String {
        return "MRV-B{" + super.toString() + ", optional=" + optional + '}'
    }

    override fun toMrz(): String {
        return "V<" + MrzParser.toMrz(issuingCountry, 3) +
                MrzParser.nameToMrz(surname, givenNames, 31) +
                '\n' +
                // second line
                MrzParser.toMrz(documentNumber, 9) +
                MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 9)) +
                MrzParser.toMrz(nationality, 3) +
                dateOfBirth!!.toMrz() +
                MrzParser.computeCheckDigitChar(dateOfBirth!!.toMrz()) +
                sex!!.mrz +
                expirationDate!!.toMrz() +
                MrzParser.computeCheckDigitChar(expirationDate!!.toMrz()) +
                MrzParser.toMrz(optional, 8) +
                '\n'
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
