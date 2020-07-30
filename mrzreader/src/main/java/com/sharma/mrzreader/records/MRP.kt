package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzFormat


/**
 * MRP Passport format: A two line long, 44 characters per line format.
 */
class MRP : MrzRecord(MrzFormat.PASSPORT) {
    /**
     * personal number (may be used by the issuing country as it desires), 14 characters long.
     */
    var personalNumber: String = ""

    var validPersonalNumber: Boolean = false

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
        personalNumber = parser.parseString(MrzRange(28, 42, 1))
        validPersonalNumber = parser.checkDigit(42, 1, MrzRange(28, 42, 1), "personal number")
        validComposite = parser.checkDigit(
                43,
                1,
                parser.rawValue(MrzRange(0, 10, 1), MrzRange(13, 20, 1), MrzRange(21, 43, 1)),
                "mrz"
        )
    }

    override fun toString(): String {
        return "MRP{" + super.toString() + ", personalNumber=" + personalNumber + '}'
    }

    override fun toMrz(): String {
        // first line
        val sb = StringBuilder()
        sb.append(code1)
        sb.append(code2)
        sb.append(MrzParser.toMrz(issuingCountry, 3))
        sb.append(MrzParser.nameToMrz(surname, givenNames, 39))
        sb.append('\n')
        // second line
        val docNum = MrzParser.toMrz(documentNumber, 9) + MrzParser.computeCheckDigitChar(
                MrzParser.toMrz(
                        documentNumber,
                        9)
        )
        sb.append(docNum)
        sb.append(MrzParser.toMrz(nationality, 3))
        val dob = dateOfBirth!!.toMrz() + MrzParser.computeCheckDigitChar(dateOfBirth!!.toMrz())
        sb.append(dob)
        sb.append(sex!!.mrz)
        val edpn =
                expirationDate!!.toMrz() + MrzParser.computeCheckDigitChar(expirationDate!!.toMrz()) + MrzParser.toMrz(
                        personalNumber,
                        14
                ) + MrzParser.computeCheckDigitChar(MrzParser.toMrz(personalNumber, 14))
        sb.append(edpn)
        sb.append(MrzParser.computeCheckDigitChar(docNum + dob + edpn))
        sb.append('\n')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
