package se.ju.student.hihe1788.laderappen2

/**
 * Represent the mowers current state
 * @property mThrust: Amount of thrust that the mower shall have
 * @property mTurn: Amount of turn that the mower shall turn with
 * @property mLight: If the mowers light shall be on/off
 * @property mHonk: If the mower shall honk or not
 * @property mAuto: Sets either autonomous or manual mode
 */
object DriveInstructionsModel {
    private const val MIN_THRUST = -100f
    private const val MAX_THRUST = 100f

    private const val MIN_TURN = -100f
    private const val MAX_TURN = 100f

    private var mThrust: Int = 0
    private var mTurn: Int = 0
    private var mLight: Int = 0
    private var mHonk: Int = 1
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

    /**
     * Toggle [mLight] between 1 (on) or 0 (off)
     */
    fun toggleLight() {
        mLight = if (mLight == 1) 0 else 1
    }

    /**
     * @return A command that the mower interpret
     * as to turn the lights on
     */
    fun getLightAsByteArray() : ByteArray {
        return "@L,$mLight,0$".toByteArray()
    }

    /**
     * @return A command that the mower interpret to honk
     */
    fun getHonkAsByteArray() : ByteArray {
        return "@H,$mHonk,0$".toByteArray()
    }

    /**
     * Toggle between autonomous drive mode and manual.
     */
    fun toggleDriveMode() {
        mAuto = if(mAuto == 1) 0 else 1
    }

    /**
     * @return Either autonomous drive mode or manual as ByteArray
     */
    fun getDriveModeAsByteArray() : ByteArray {
        return if (mAuto == 1) {
            "@A,,0$".toByteArray()
        } else {
            "@M,,0$".toByteArray()
        }
    }

    /**
     * Turn of the Mower
     * @return A command that the mower interpret to shutdown.
     */
    fun getTurnOffCmdAsByteArray() : ByteArray {
        return "@Q,,0".toByteArray()
    }

    /**
     * Only sends thrust and steer.
     * @return A command that the mower interpret as drive instruction.
     */
    fun toByteArray(): ByteArray {
        return "@D,$mThrust;$mTurn,0$".toByteArray()
    }

}