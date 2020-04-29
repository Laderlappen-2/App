package se.ju.student.hihe1788.laderappen2

import se.ju.student.hihe1788.laderappen2.Constants.COLL_EVENT_ACK
import se.ju.student.hihe1788.laderappen2.Constants.LIGHT_ACK
import se.ju.student.hihe1788.laderappen2.Constants.POS_EVENT_ACK
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@ExperimentalTime
class IncomingMessage(bytes: ByteArray) {

    val mTypeOfMessage: String
    var mIsACK = false
    var mXPos: Int? = null
    var mYPos: Int? = null

    var mTimestamp: Duration? = null
    val mPattern = Regex("[^@,$]+")

    init {
        val data = bytes.toString()
        val rawData = mPattern.findAll(data).toList()
        mTypeOfMessage = rawData[0].value

        when(mTypeOfMessage) {
            LIGHT_ACK -> mIsACK = true
            POS_EVENT_ACK,
            COLL_EVENT_ACK -> {
                mXPos = rawData[1].value.toInt()
                mYPos = rawData[2].value.toInt()
                mTimestamp = rawData[3].value.toInt().milliseconds
            }
        }
    }
}

// @L,A$
//@event,X,Y,klocka$
// @event,1,1,54321$
// @event,32.4,34.2,54321$


// [^@,$]+