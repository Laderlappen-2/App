package se.ju.student.hihe1788.laderappen2.util

enum class SteeringModeEnum(val value: Int) {
    LEFT_HANDED(0),
    RIGHT_HANDED(1),
    TWO_HANDED(2);

    companion object {
        fun fromValue(value: Int): SteeringModeEnum? {
            return values().find { it.value == value }
        }
    }

}