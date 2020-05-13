package se.ju.student.hihe1788.laderappen2

/**
 * An object that handles all constants.
 */
object Constants {
    val STATE_NONE = 0         // we're doing nothing
    val STATE_LISTEN = 1      // now listening for incoming connectings
    val STATE_CONNECTING = 2  // now initiating an outgoing connecting
    val STATE_CONNECTED = 3   // now connected to a remote device

    // Message types sent from the BluetoothService Handler
    val MESSAGE_STATE_CHANGE = 1
    val MESSAGE_READ = 2
    val MESSAGE_WRITE = 3
    val MESSAGE_TOAST = 4

    // Key names received from the BluetoothService Handler
    val TOAST = "toast"

    // ACTION INTENTS
    val ACTION_STATE_NONE = "0"
    val ACTION_STATE_LISTEN = "1"
    val ACTION_STATE_CONNECTING = "2"
    val ACTION_STATE_CONNECTED = "3"

    val ACTION_ALERT = "4"

    val ACTION_MSG_RECEIVED = "5"

    const val COLLISION_AVOIDANCE_POINT = 3
    const val POSITION_POINT = 5

    const val OFFICIAL_DOC_ANDROID_DEVELOPER = "https://developer.android.com/docs"
}
