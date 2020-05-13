package se.ju.student.hihe1788.laderappen2

import android.util.Log

private val TAG = DriveInstructionsModel::class.java.simpleName
/**
 * Represent the mowers current state
 */
object DriveInstructionsModel {
    private const val MIN_THRUST = -100f
    private const val MAX_THRUST = 100f

    private const val MIN_TURN = -100f
    private const val MAX_TURN = 100f


    private var mThrust: Int = 0
    private var mTurn: Int = 0
    private var mLight: Int = 0
    private var mHonk: Int = 0
    private var mAuto: Int = 0

    /**
     * @param percent sets a new thrust
     */
    fun setThrust(percent: Float) {
        val newThrust = MAX_THRUST * percent

        mThrust = when {
            newThrust < MIN_THRUST -> MIN_THRUST.toInt()
            newThrust > MAX_THRUST -> MAX_THRUST.toInt()
            else -> newThrust.toInt()
        }


    }

    /**
     * @param percent sets a new turn
     */
    fun setTurn(percent: Float) {
        val newTurn = MAX_TURN * percent

        mTurn = when {
            newTurn < MIN_TURN -> MIN_TURN.toInt()
            newTurn > MAX_TURN -> MAX_TURN.toInt()
            else -> newTurn.toInt()
        }

    }

    fun setLightOn() {
        mLight = 1
    }

    fun setLightOff() {
        mLight = 0
    }

    fun getLightAsByteArray() : ByteArray {
        return "@L,$mLight,0$".toByteArray()
    }

    fun setHonkOn() {
        mHonk = 1
    }

    fun getHonkAsByteArray() : ByteArray {
        return "@H,$mHonk,0$".toByteArray()
    }

    fun setAutoOn() {
        mAuto = 1
    }

    fun setAutoOff() {
        mAuto = 0
    }

    fun getAutonomousModeAsByteArray() : ByteArray {
        return if (mAuto == 1) {
            "@A,,0$".toByteArray()
        } else {
            "@M,,0$".toByteArray()
        }
    }

    fun getTurnOffCmdAsByteArray() : ByteArray {
        return "@Q,,0".toByteArray()
    }

    /**
     * Only sends thrust and steer.
     */
    fun toByteArray(): ByteArray {
        return "@D,$mThrust;$mTurn,0$".toByteArray()
    }

    /**
     * Translates a instruction to the mower according to our
     * message protoC00L.
     * “@Left/Right, Back/Forward, Light, Honk$”
     * @: start of message
     * Left/Right: -100:100
     * Back/Forward: -100:100
     * Light: 0 or 1
     * Honk: 0 or 1
     * $: end of message
     * @return ByteArray consisting of the new instruction(s)

    fun toByteArray(): ByteArray {
    val bArr = "@$mThrust;$mTurn;$mLight;$mHonk;$mAuto$".toByteArray()
    mHonk = 0
    return bArr
    }
     */
}