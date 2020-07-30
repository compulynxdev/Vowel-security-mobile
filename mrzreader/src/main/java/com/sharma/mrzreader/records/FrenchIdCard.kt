package com.sharma.mrzreader.records

import com.sharma.mrzreader.MrzParser
import com.sharma.mrzreader.MrzRange
import com.sharma.mrzreader.MrzRecord
import com.sharma.mrzreader.types.MrzDocumentCode
import com.sharma.mrzreader.types.MrzFormat

/**
 * Format used for French ID Cards.
 *
 *
 * The structure of the card:
 * 2 lines of 36 characters :
 * <pre>First line : IDFRA{name}{many < to complete line}{6 numbers unknown}
 * Second line : {card number on 12 numbers}{Check digit}{given names separated by "<<" and maybe troncated if too long}{date of birth YYMMDD}{Check digit}{sex M/F}{1 number checksum}</pre>
 */
class FrenchIdCard : MrzRecord(MrzFormat.FRENCH_ID) {
    init {
        code = MrzDocumentCode.TypeI
        code1 = 'I'
        code2 = 'D'
    }

    /**
     * For use of the issuing State or
     * organization.
    ry      */
    //public String optional;

    override fun fromMrz(mrz: String) {
        super.fromMrz(mrz)
        val p = MrzParser(mrz)
        //Special because surname and firstname not on the same line
        val name = arrayOf("", "")
        name[0] = p.parseString(MrzRange(5, 30, 0))
        name[1] = p.parseString(MrzRange(13, 27, 1))
        setName(name)
        nationality = p.parseString(MrzRange(2, 5, 0))
        optional = p.parseString(MrzRange(30, 36, 0))
        documentNumber = p.parseString(MrzRange(0, 12, 1))
        validDocumentNumber = p.checkDigit(12, 1, MrzRange(0, 12, 1), "document number")
        dateOfBirth = p.parseDate(MrzRange(27, 33, 1))
        validDateOfBirth =
                p.checkDigit(33, 1, MrzRange(27, 33, 1), "date of birth") && dateOfBirth!!.isDateValid
        sex = p.parseSex(34, 1)
        val finalChecksum = mrz.replace("\n", "").substring(0, 36 + 35)
        validComposite = p.checkDigit(35, 1, finalChecksum, "final checksum")
        // TODO expirationDate is missing
    }

    override fun toString(): String {
        return "FrenchIdCard{" + super.toString() + ", optional=" + optional + '}'
    }

    override fun toMrz(): String {
        val sb = StringBuilder("IDFRA")
        // first row
        sb.append(MrzParser.toMrz(surname, 25))
        sb.append(MrzParser.toMrz(optional, 6))
        sb.append('\n')
        // second row
        sb.append(MrzParser.toMrz(documentNumber, 12))
        sb.append(MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 12)))
        sb.append(MrzParser.toMrz(givenNames, 14))
        sb.append(dateOfBirth!!.toMrz())
        sb.append(MrzParser.computeCheckDigitChar(dateOfBirth!!.toMrz()))
        sb.append(sex!!.mrz)
        sb.append(MrzParser.computeCheckDigitChar(sb.toString().replace("\n", "")))
        sb.append('\n')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
