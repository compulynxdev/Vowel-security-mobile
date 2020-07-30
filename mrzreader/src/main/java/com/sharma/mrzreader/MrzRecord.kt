package com.sharma.mrzreader

import com.sharma.mrzreader.types.MrzDate
import com.sharma.mrzreader.types.MrzDocumentCode
import com.sharma.mrzreader.types.MrzFormat
import com.sharma.mrzreader.types.MrzSex
import java.io.Serializable

/**
 * An abstract MRZ record, contains basic information present in all MRZ record types.
 */
abstract class MrzRecord protected constructor(
        /**
         * Detected MRZ format.
         */
        val format: MrzFormat
) : Serializable {


    /**
     * The document code.
     */
    var code: MrzDocumentCode? = null

    /**
     * Document code, see [MrzDocumentCode] for details on allowed values.
     */
    var code1: Char = ' '

    /**
     * For MRTD: Type, at discretion of states, but 1-2 should be IP for passport card, AC for crew member and IV is not allowed.
     * For MRP: Type (for countries that distinguish between different types of passports)
     */
    var code2: Char = ' '


    /**
     * An [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) country code of issuing country, with additional allowed values (according to [article on Wikipedia](http://en.wikipedia.org/wiki/Machine-readable_passport)):
     *  * D: Germany
     *  * GBD: British dependent territories citizen(note: the country code of the overseas territory is presently used to indicate issuing authority and nationality of BOTC)
     *  * GBN: British National (Overseas)
     *  * GBO: British Overseas citizen
     *  * GBP: British protected person
     *  * GBS: British subject
     *  * UNA: specialized agency of the United Nations
     *  * UNK: resident of Kosovo to whom a travel document has been issued by the United Nations Interim Administration Mission in Kosovo (UNMIK)
     *  * UNO: United Nations Organization
     *  * XOM: Sovereign Military Order of Malta
     *  * XXA: stateless person, as per the 1954 Convention Relating to the Status of Stateless Persons
     *  * XXB: refugee, as per the 1951 Convention Relating to the Status of Refugees
     *  * XXC: refugee, other than defined above
     *  * XXX: unspecified nationality
     */
    var issuingCountry: String = ""

    /**
     * Document number, e.g. passport number.
     */
    var documentNumber: String? = null

    /**
     * The surname in uppercase.
     */
    var surname: String = ""

    /**
     * The given names in uppercase, separated by spaces.
     */
    var givenNames: String = ""

    /**
     * Date of birth.
     */
    var dateOfBirth: MrzDate? = null

    /**
     * Sex
     */
    var sex: MrzSex? = null

    /**
     * expiration date of passport
     */
    var expirationDate: MrzDate? = null

    /**
     * An [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) country code of nationality.
     * See [.issuingCountry] for additional allowed values.
     */
    var nationality: String? = null

    /**
     * Optional data at the discretion of the issuing State
     */
    var optional = ""

    /**
     * optional (for U.S. passport holders, 21-29 may be corresponding passport number)
     */
    var optional2 = ""

    /**
     * check digits, usually common in every document.
     */
    var validDocumentNumber = true
    var validDateOfBirth = true
    var validExpirationDate = true
    var validComposite = true

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n").append("Code :- ").append(code).append("[").append(code1)
                .append(code2).append("]")
                .append("\n").append("issuingCountry :- ").append(issuingCountry)
                .append("\n").append("documentNumber :- ").append(documentNumber)
                .append("\n").append("surname :- ").append(surname)
                .append("\n").append("givenNames :- ").append(givenNames)
                .append("\n").append("dateOfBirth :- ").append(dateOfBirth)
                .append("\n").append("sex :- ").append(sex)
                .append("\n").append("expirationDate :- ").append(expirationDate)
                .append("\n").append("nationality :- ").append(nationality)
        return stringBuilder.toString()
        /*return "{" + "code=" + code + "[" + code1 + code2 + "], issuingCountry=" + issuingCountry + ", documentNumber=" + documentNumber
                + ", surname=" + surname + ", givenNames=" + givenNames + ", dateOfBirth=" + dateOfBirth + ", sex=" + sex + ", expirationDate="
                + expirationDate + ", nationality=" + nationality + '}';*/
    }

    /**
     * Parses the MRZ record.
     * @param mrz the mrz record, not null, separated by \n
     * @throws MrzParseException when a problem occurs.
     */
    @Throws(MrzParseException::class)
    open fun fromMrz(mrz: String) {
        if (format !== MrzFormat.get(mrz)) {
            throw MrzParseException(
                    "invalid format: " + MrzFormat.get(mrz),
                    mrz,
                    MrzRange(0, 0, 0),
                    format
            )
        }
        code = MrzDocumentCode.parse(mrz)
        code1 = mrz[0]
        code2 = mrz[1]
        issuingCountry = MrzParser(mrz).parseString(MrzRange(2, 5, 0))
    }

    /**
     * Helper method to set the full name. Changes both [.surname] and [.givenNames].
     * @param name expected array of length 2, in the form of [surname, first_name]. Must not be null.
     */
    protected fun setName(name: Array<String>) {
        surname = name[0]
        givenNames = name[1]
    }

    /**
     * Serializes this record to a valid MRZ record.
     * @return a valid MRZ record, not null, separated by \n
     */
    abstract fun toMrz(): String
}
