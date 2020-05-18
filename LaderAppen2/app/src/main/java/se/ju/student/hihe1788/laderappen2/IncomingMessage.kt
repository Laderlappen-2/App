package se.ju.student.hihe1788.laderappen2

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException


private val TAG = IncomingMessage::class.java.simpleName

/**
 * A class that handles all incoming message from the mower
 * and maps it to accordingly fields.
 * @param bytes: Raw data received from mower.
 */
@RequiresApi(Build.VERSION_CODES.O)
class IncomingMessage(bytes: ByteArray) {

    var mTypeOfMessage: String? = null
    var mIsACK = false
    var mXPos: Float? = null
    var mYPos: Float? = null



    init {
        try {
            val rawData = bytes.toString(Charsets.UTF_8)


            Log.i(TAG, "IncomingMsgRAW: $rawData")
            val data = trimString(rawData)
            if (data.isEmpty()) {
                throw ParseIncomingMsgException("Garbage message")
            }
            mTypeOfMessage = data[1]

            when(mTypeOfMessage) {
                "L",
                "A" -> {
                    mIsACK = true
                }
                "0",
                "1" -> {
                    mXPos = data[2].toFloat()
                    mYPos = data[3].toFloat()


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

        return if (s.contains("@") && s.contains("$")) {
            if (s.contains(";")) {
                s.split("@", ",", ";", "$").map {
                    it.trim()
                }
            } else {
                s.split("@", ",", "$").map {
                    it.trim()
                }
            }
        } else {
            emptyList()
        }
    }
}

/**
 * Exception-class for parsing incoming message.
 * @param message: A exception message
 */
class ParseIncomingMsgException(message: String): Exception(message)

