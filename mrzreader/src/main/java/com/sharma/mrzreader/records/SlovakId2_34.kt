package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzFormat


/**
 * Unknown 2 line/34 characters per line format, used with old Slovak ID cards.
 */
class SlovakId2_34 : MrzRecord(MrzFormat.SLOVAK_ID_234) {
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
        setName(p.parseName(MrzRange(5, 34, 0)))
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
        optional = p.parseString(MrzRange(28, 34, 1))
        // TODO validComposite missing? (final MRZ check digit)
    }

    override fun toString(): String {
        return "SlovakId2x34{" + super.toString() + ", optional=" + optional + '}'
    }

    override fun toMrz(): String {
        // first line
        return "" + code1 +
                code2 +
                MrzParser.toMrz(issuingCountry, 3) +
                MrzParser.nameToMrz(surname, givenNames, 29) +
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
                MrzParser.toMrz(optional, 6) +
                '\n'
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
