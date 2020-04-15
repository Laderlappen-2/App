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
    Left/Right: -250:250
    Back/Forward: -250:250
    Light: 0 eller 1
    Honk: 0 eller 1
    $: end of message
    */

    fun instructionsToMessage(): ByteArray {
        return "@$mTurn,$mThrust$".toByteArray()
    }

}