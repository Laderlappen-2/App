package se.ju.student.hihe1788.laderappen2

import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

/**
 * A class that handles all incoming message from the mower
 * and maps it to accordingly fields.
 * @param bytes: Raw data received from mower.
 */
@ExperimentalTime
class IncomingMessage(bytes: ByteArray) {

    var mTypeOfMessage: String? = null
    var mIsACK = false
    var mXPos: Int? = null
    var mYPos: Int? = null

    var mTimestamp: Duration? = null

    init {
        try {
            val rawData = bytes.toString(Charset.defaultCharset())
            val data = trimString(rawData)
            if (data.isEmpty()) {
                throw ParseIncomingMsgException("Garbage message")
            }
            mTypeOfMessage = data[0]

            when(mTypeOfMessage) {
                "L",
                "A" -> {
                    mIsACK = true
                }
                "0",
                "1" -> {
                    mXPos = data[1].toInt()
                    mYPos = data[2].toInt()
                    mTimestamp = data[3].toInt().milliseconds

                    /* Speak the same language as the REST-API */
                    if (mTypeOfMessage == "0") mTypeOfMessage = "5" // PositionEvent
                    else mTypeOfMessage = "3"                       // CollisionAvoidanceEvent
                }
            }
        } catch (e: IOException) {
            throw ParseIncomingMsgException(e.message!!)
        }
    }

    /**
     * Trims the incoming message and returns it in a
     * workable way.
     * @param s: String in need of to be trimmed.
     * @return A list of substrings that represent fields
     * such as [mXPos] etc.
     * @throws ParseIncomingMsgException
     */
    private fun trimString(s: String): List<String> {
        try {
            if (s.contains("@") && s.contains("$")) {
                if (s.contains(";")) {
                    return s.split("@", ",", ";", "$")
                } else {
                    return s.split("@", ",", "$")
                }
            }
        } catch (e: Exception) {
            throw ParseIncomingMsgException("failed")
        } finally {
            return emptyList()
        }


    }
}

/**
 * Exception-class for parsing incoming message.
 * @param message: A exception message
 */
class ParseIncomingMsgException(message: String): Exception(message)

