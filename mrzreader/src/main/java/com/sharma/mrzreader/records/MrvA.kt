package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzDocumentCode
import com.sharma.mrzreader.types.MrzFormat

/**
 * MRV type-A format: A two lines long, 44 characters per line format
 */
class MrvA : MrzRecord(MrzFormat.MRV_VISA_A) {
    init {
        code1 = 'V'
        code2 = '<'
        code = MrzDocumentCode.TypeV
    }

    /**
     * Optional data at the discretion of the issuing State
     */
    //public String optional;

    override fun fromMrz(mrz: String) {
        super.fromMrz(mrz)
        val parser = MrzParser(mrz)
        setName(parser.parseName(MrzRange(5, 44, 0)))
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
        optional = parser.parseString(MrzRange(28, 44, 1))
        // TODO validComposite missing? (final MRZ check digit)
    }

    override fun toString(): String {
        return "MRV-A{" + super.toString() + ", optional=" + optional + '}'
    }

    override fun toMrz(): String {
        // first line
        return "V<" + MrzParser.toMrz(issuingCountry, 3) +
                MrzParser.nameToMrz(surname, givenNames, 39) +
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
                MrzParser.toMrz(optional, 16) +
                '\n'
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
