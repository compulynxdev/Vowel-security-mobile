package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzFormat


/**
 * MRTD TD1 format: A three line long, 30 characters per line format.
 */
class MrtdTd1 : MrzRecord(MrzFormat.MRTD_TD1) {
    /**
     * Optional data at the discretion
     * of the issuing State. May contain
     * an extended document number
     * as per 6.7, note (j).
     */
    //public String optional;
    /**
     * optional (for U.S. passport holders, 21-29 may be corresponding passport number)
     */
    //public String optional2;

    override fun fromMrz(mrz: String) {
        super.fromMrz(mrz)
        val p = MrzParser(mrz)
        documentNumber = p.parseString(MrzRange(5, 14, 0))
        validDocumentNumber = p.checkDigit(14, 0, MrzRange(5, 14, 0), "document number")
        optional = p.parseString(MrzRange(15, 30, 0))
        dateOfBirth = p.parseDate(MrzRange(0, 6, 1))
        validDateOfBirth =
                p.checkDigit(6, 1, MrzRange(0, 6, 1), "date of birth") && dateOfBirth!!.isDateValid
        sex = p.parseSex(7, 1)
        expirationDate = p.parseDate(MrzRange(8, 14, 1))
        validExpirationDate = p.checkDigit(
                14,
                1,
                MrzRange(8, 14, 1),
                "expiration date"
        ) && expirationDate!!.isDateValid
        nationality = p.parseString(MrzRange(15, 18, 1))
        optional2 = p.parseString(MrzRange(18, 29, 1))
        validComposite = p.checkDigit(
                29,
                1,
                p.rawValue(
                        MrzRange(5, 30, 0),
                        MrzRange(0, 7, 1),
                        MrzRange(8, 15, 1),
                        MrzRange(18, 29, 1)
                ),
                "mrz"
        )
        setName(p.parseName(MrzRange(0, 30, 2)))
    }

    override fun toString(): String {
        return "MRTD-TD1{" + super.toString() + ", optional=" + optional + ", optional2=" + optional2 + '}'
    }

    override fun toMrz(): String {
        // first line
        val sb = StringBuilder()
        sb.append(code1)
        sb.append(code2)
        sb.append(MrzParser.toMrz(issuingCountry, 3))
        val dno = MrzParser.toMrz(documentNumber, 9) + MrzParser.computeCheckDigitChar(
                MrzParser.toMrz(
                        documentNumber,
                        9
                )
        ) + MrzParser.toMrz(optional, 15)
        sb.append(dno)
        sb.append('\n')
        // second line
        val dob = dateOfBirth!!.toMrz() + MrzParser.computeCheckDigitChar(dateOfBirth!!.toMrz())
        sb.append(dob)
        sb.append(sex!!.mrz)
        val ed = expirationDate!!.toMrz() + MrzParser.computeCheckDigitChar(expirationDate!!.toMrz())
        sb.append(ed)
        sb.append(MrzParser.toMrz(nationality, 3))
        sb.append(MrzParser.toMrz(optional2, 11))
        sb.append(MrzParser.computeCheckDigitChar(dno + dob + ed + MrzParser.toMrz(optional2, 11)))
        sb.append('\n')
        // third line
        sb.append(MrzParser.nameToMrz(surname, givenNames, 30))
        sb.append('\n')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
