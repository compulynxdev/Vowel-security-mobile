package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzFormat


/**
 * MRTD td2 format: A two line long, 36 characters per line format.
 */
class MrtdTd2 : MrzRecord(MrzFormat.MRTD_TD2) {
    /**
     * For use of the issuing State or
     * organization. Unused character positions
     * shall be completed with filler characters (&lt;)
     * repeated up to position 35 as required.
     */
    //public String optional;

    override fun fromMrz(mrz: String) {
        super.fromMrz(mrz)
        val p = MrzParser(mrz)
        setName(p.parseName(MrzRange(5, 36, 0)))
        documentNumber = p.parseString(MrzRange(0, 9, 1))
        validDocumentNumber = p.checkDigit(9, 1, MrzRange(0, 9, 1), "document number")
        nationality = p.parseString(MrzRange(10, 13, 1))
        dateOfBirth = p.parseDate(MrzRange(13, 19, 1))
        validDateOfBirth =
                p.checkDigit(19, 1, MrzRange(13, 19, 1), "date of birth") && dateOfBirth!!.isDateValid
        sex = p.parseSex(20, 1)
        expirationDate = p.parseDate(MrzRange(21, 27, 1))
        validExpirationDate = p.checkDigit(
                27,
                1,
                MrzRange(21, 27, 1),
                "expiration date"
        ) && expirationDate!!.isDateValid
        optional = p.parseString(MrzRange(28, 35, 1))
        validComposite = p.checkDigit(
                35,
                1,
                p.rawValue(MrzRange(0, 10, 1), MrzRange(13, 20, 1), MrzRange(21, 35, 1)),
                "mrz"
        )
    }

    override fun toString(): String {
        return "MRTD-TD2{" + super.toString() + ", optional=" + optional + '}'
    }

    override fun toMrz(): String {
        // first line
        val sb = StringBuilder()
        sb.append(code1)
        sb.append(code2)
        sb.append(MrzParser.toMrz(issuingCountry, 3))
        sb.append(MrzParser.nameToMrz(surname, givenNames, 31))
        sb.append('\n')
        // second line
        val dn = MrzParser.toMrz(documentNumber, 9) + MrzParser.computeCheckDigitChar(
                MrzParser.toMrz(
                        documentNumber,
                        9
                )
        )
        sb.append(dn)
        sb.append(MrzParser.toMrz(nationality, 3))
        val dob = dateOfBirth!!.toMrz() + MrzParser.computeCheckDigitChar(dateOfBirth!!.toMrz())
        sb.append(dob)
        sb.append(sex?.mrz)
        val ed = expirationDate!!.toMrz() + MrzParser.computeCheckDigitChar(expirationDate!!.toMrz())
        sb.append(ed)
        sb.append(MrzParser.toMrz(optional, 7))
        sb.append(MrzParser.computeCheckDigitChar(dn + dob + ed + MrzParser.toMrz(optional, 7)))
        sb.append('\n')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
