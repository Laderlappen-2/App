package se.ju.student.hihe1788.laderappen2.util

/**
 * A class that holds enums for the steering mode.
 * @property value The value for the enum
 */
enum class SteeringModeEnum(val value: Int) {
    LEFT_HANDED(0),
    RIGHT_HANDED(1),
    TWO_HANDED(2);

    companion object {
        /**
         * Returns the SteeringModeEnum from its value
         * @param value The value of the steering mode enum
         */
        fun fromValue(value: Int): SteeringModeEnum? {
            return values().find { it.value == value }
        }
    }

}