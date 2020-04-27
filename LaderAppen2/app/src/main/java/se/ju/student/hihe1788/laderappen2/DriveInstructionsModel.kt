package se.ju.student.hihe1788.laderappen2

class DriveInstructionsModel {
    private val MIN_THRUST = -100
    private val MAX_THRUST = 100
    private val MIN_TURN = -100
    private val MAX_TURN = 100

    var mThrust: Int = 0
    var mTurn: Int = 0

    fun setInstructions(thrust: Int, turn: Int) {
        setThrust(thrust)
        setTurn(turn)
    }

    fun setThrust(thrust: Int) {
        if (thrust < MIN_THRUST)
            mThrust = MIN_THRUST
        else if (thrust > MAX_THRUST)
            mThrust = MAX_THRUST
        else
            mThrust = thrust
    }

    fun setTurn(turn: Int) {
        if (turn < MIN_TURN)
            mThrust = MIN_TURN
        else if (turn > MAX_TURN)
            mThrust = MAX_TURN
        else
            mThrust = turn
    }


    /*
    “@Left/Right,Back/Forward,Light,Honk$”
    @: start of message
    Left/Right: -100:100
    Back/Forward: -100:100
    Light: 0 / 1
    Honk: 0 / 1
    $: end of message
    */
    fun instructionsToMessage(): ByteArray {
        return "@$mTurn,$mThrust$".toByteArray()
    }

}