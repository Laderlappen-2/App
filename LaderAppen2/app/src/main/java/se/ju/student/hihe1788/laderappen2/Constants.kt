package se.ju.student.hihe1788.laderappen2

object Constants {
    val STATE_NONE = 0      // we're doing nothing
    val STATE_LISTEN = 1      // now listening for incoming connectings
    val STATE_CONNECTING = 2  // now initiating an outgoing connecting
    val STATE_CONNECTED = 3   // now connected to a remote device

    // Message types sent from the BluetoothService Handler
    val MESSAGE_STATE_CHANGE = 1
    val MESSAGE_READ = 2
    val MESSAGE_WRITE = 3
    val MESSAGE_DEVICE_NAME = 4
    val MESSAGE_TOAST = 5

    // Key names received from the BluetoothService Handler
    val DEVICE_NAME = "device_name"
    val TOAST = "toast"

    // Intent request codes
    val REQUEST_CONNECT_DEVICE_SECURE = 1
    val REQUEST_CONNECT_DEVICE_INSECURE = 2
    val REQUEST_ENABLE_BT = 3

    // ACTION INTENTS
    val ACTION_STATE_NONE = "0"
    val ACTION_STATE_LISTEN = "1"
    val ACTION_STATE_CONNECTING = "2"
    val ACTION_STATE_CONNECTED = "3"

    val ACTION_ALERT = "4"

    val ACTION_MSG_RECEIVED = "5"
}
