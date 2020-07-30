package com.sharma.mrzreader.types

import com.sharma.mrzreader.MrzParseException
import com.sharma.mrzreader.MrzRange

/**
 * Lists all supported MRZ record types (a.k.a. document codes).
 */
enum class MrzDocumentCode {

    /**
     * A passport, P or IP.
     * ... maybe Travel Document that is very similar to the passport.
     */
    Passport,

    /**
     * General I type (besides IP).
     */
    TypeI,

    /**
     * General A type (besides AC).
     */
    TypeA,

    /**
     * Crew member (AC).
     */
    CrewMember,

    /**
     * General type C.
     */
    TypeC,

    /**
     * Type V (Visa).
     */
    TypeV,

    /**
     *
     */
    Migrant;


    companion object {

        /**
         * turning to switch statement due to lots of types
         *
         * @param mrz
         * @return
         */
        fun parse(mrz: String): MrzDocumentCode {
            val code = mrz.substring(0, 2)

            // 2-letter checks
            when (code) {
                "IV" -> throw MrzParseException(
                        "IV document code is not allowed",
                        mrz,
                        MrzRange(0, 2, 0),
                        null
                ) // TODO why?
                "AC" -> return CrewMember
                "ME" -> return Migrant
                "TD" -> return Migrant // travel document
                "IP" -> return Passport
            }

            // 1-letter checks
            when (code[0]) {
                'T'   // usually Travel Document
                    , 'P' -> return Passport
                'A' -> return TypeA
                'C' -> return TypeC
                'V' -> return TypeV
                'I' -> return TypeI // identity card or residence permit
                'R' -> return Migrant  // swedish '51 Convention Travel Document
            }


            throw MrzParseException(
                    "Unsupported document code: $code",
                    mrz,
                    MrzRange(0, 2, 0),
                    null
            )
        }
    }
}
