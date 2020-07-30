package com.sharma.mrzreader

import com.sharma.mrzreader.types.MrzFormat

/**
 * Thrown when a MRZ parse fails.
 */
class MrzParseException(
        message: String,
        /**
         * The MRZ string being parsed.
         */
        val mrz: String,
        /**
         * Range containing problematic characters.
         */
        val range: MrzRange,
        /**
         * Expected MRZ format.
         */
        val format: MrzFormat?
) : RuntimeException("Failed to parse MRZ $format $mrz at $range: $message") {
    companion object {

        private val serialVersionUID = 1L
    }
}