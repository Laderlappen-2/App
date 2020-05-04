package se.ju.student.hihe1788.laderappen2

/**
 * Represent the mowers current state
 */
class DriveInstructionsModel {
    private val MIN_THRUST = -100
    private val MAX_THRUST = 100
    private val MIN_TURN = -100
    private val MAX_TURN = 100

    var mThrust: Int = 0
    var mTurn: Int = 0

    /**
     * @param thrust A given thrust from -100:100 (maxBack:maxForward)
     * @param turn A given turn-value from -100:100 (maxLeft:maxRight)
     */
    fun setInstructions(thrust: Int, turn: Int) {
        setThrust(thrust)
        setTurn(turn)
    }

    /**
     * @param thrust sets a new thrust
     */
    fun setThrust(thrust: Int) {
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
    fun setTurn(turn: Int) {
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
    fun instructionsToMessage(): ByteArray {
        return "@$mTurn,$mThrust$".toByteArray()
    }

}