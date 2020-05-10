package se.ju.student.hihe1788.laderappen2

import android.util.Log

private val TAG = DriveInstructionsModel::class.java.simpleName
/**
 * Represent the mowers current state
 */
object DriveInstructionsModel {
    private val MIN_THRUST = -100f
    private val MAX_THRUST = 100f
    private val NEUTRAL_THRUST= (MAX_THRUST - MIN_THRUST) / 2f
    private val MIN_TURN = -100f
    private val MAX_TURN = 100f
    private val NEUTRAL_TURN = (MAX_TURN - MIN_TURN) / 2f

    private var mThrust: Int = 0
    private var mTurn: Int = 0
    private var mLight: Int = 0
    private var mHonk: Int = 0
    private var mAuto: Int = 0

    /**
     * @param thrust sets a new thrust
     */
    fun setThrust(percent: Float) {
        val value = MAX_THRUST * percent
        val newThrust = (NEUTRAL_THRUST + value)

        if (newThrust < MIN_THRUST)
            mThrust = MIN_THRUST.toInt()
        else if (newThrust > MAX_THRUST)
            mThrust = MAX_THRUST.toInt()
        else
            mThrust = newThrust.toInt()
    }

    /**
     * @param turn sets a new turn
     */
    fun setTurn(percent: Float) {
        val value = MAX_TURN * percent
        val newTurn = (NEUTRAL_TURN + value)

        if (newTurn < MIN_TURN)
            mTurn = MIN_TURN.toInt()
        else if (newTurn > MAX_TURN)
            mTurn = MAX_TURN.toInt()
        else
            mTurn = newTurn.toInt()
    }

    fun setLightOn() {
        mLight = 1
    }

    fun setLightOff() {
        mLight = 0
    }

    fun setHonkOn() {
        mHonk = 1
    }

    fun setAutoOn() {
        mAuto = 1
    }

    fun setAutoOff() {
        mAuto = 0
    }

    /**
     * Translates a instruction to the mower according to our
     * message protoC00L.
     * “@Left/Right, Back/Forward, Light, Honk$”
     * @: start of message
     * Left/Right: -100:100
     * Back/Forward: -100:100
     * Light: 0 eller 1
     * Honk: 0 eller 1
     * $: end of message
     * @return ByteArray consisting of the new instruction(s)
     */
    fun toByteArray(): ByteArray {
        val bArr = "@$mThrust;$mTurn;$mLight;$mHonk;$mAuto$".toByteArray()
        mHonk = 0
        return bArr
    }
}