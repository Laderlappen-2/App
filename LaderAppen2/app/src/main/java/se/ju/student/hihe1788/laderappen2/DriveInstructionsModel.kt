package se.ju.student.hihe1788.laderappen2

/**
 * Represent the mowers current state
 */
object DriveInstructionsModel {
    private val MIN_THRUST = -100
    private val MAX_THRUST = 100
    private val MIN_TURN = -100
    private val MAX_TURN = 100

    var mThrust: Int = 0
    var mTurn: Int = 0

    /**
     * @param value A given value from -1:1
     * @param isThrust Sets if this new instruction is thrust or steer.
     */
    fun setInstructions(value: Float, isThrust: Boolean) {
        val transValue: Int = translateToRatio(value)
        if (isThrust) setThrust(transValue)
        else setTurn(transValue)

    }

    /**
     * @param thrust sets a new thrust
     */
    private fun setThrust(thrust: Int) {
        if (thrust < MIN_THRUST)
            mThrust = MIN_THRUST
        else if (thrust > MAX_THRUST)
            mThrust = MAX_THRUST
        else
            mThrust = thrust
    }

    /**
     * @param turn sets a new turn
     */
    private fun setTurn(turn: Int) {
        if (turn < MIN_TURN)
            mThrust = MIN_TURN
        else if (turn > MAX_TURN)
            mThrust = MAX_TURN
        else
            mThrust = turn
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
    fun toMessage(): ByteArray {
        return "@D,$mThrust;$mTurn$".toByteArray()
    }

    /**
     * Sets the joysticks value to a value that the mower understands.
     * @param value The value from the joystick
     * @return A value that the mower understands
     */
    private fun translateToRatio(value: Float) : Int {
        return (value * 100).toInt()
    }
}