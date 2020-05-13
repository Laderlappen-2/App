package se.ju.student.hihe1788.laderappen2

import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

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

            mTypeOfMessage = data[0]

            when(mTypeOfMessage) {
                "L" -> {
                    mIsACK = true
                }
                "0",
                "1" -> {
                    val positions = data[1].split(";")
                    mXPos = positions[0].toInt()
                    mYPos = positions[1].toInt()
                    mTimestamp = data[2].toInt().milliseconds

                    /* Speak the same language as the REST-API */
                    if (mTypeOfMessage == "0") mTypeOfMessage = "3" // PositionEvent
                    else mTypeOfMessage = "5"                       // CollisionAvoidanceEvent
                }
            }
        } catch (e: IOException) {
            throw ParseIncomingMsgException(e.message!!)
        }
    }

    private fun trimString(s: String): List<String> {
        try {
            var trimmed1 = s.split("@")[1]
            var trimmed2 = trimmed1.split("$")[0]
            return trimmed2.split(",")
        } catch (e: IOException) {
            Log.i("Incoming message", "failed")
        }
            return listOf<String>()
    }
}

class ParseIncomingMsgException(message: String): Exception(message)

