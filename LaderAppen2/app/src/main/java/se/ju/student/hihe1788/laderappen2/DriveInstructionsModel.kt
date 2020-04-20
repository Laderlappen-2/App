package se.ju.student.hihe1788.laderappen2

class DriveInstructionsModel {
    var mThrust: Int = 0
    var mTurn: Int = 0

    fun setInstructions(thrust: Int, turn: Int) {
        mThrust = thrust
        mTurn = turn
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