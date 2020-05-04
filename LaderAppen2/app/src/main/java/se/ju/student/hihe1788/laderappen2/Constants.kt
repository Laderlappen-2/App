package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

/**
 * A place store all constants.
 */

val MOWER_SERVICE_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
val APP_UUID = UUID.fromString("0a1bb95f-c32e-4718-92a8-0b88c25b1035")

val MOWER_READ_CHARACTERISTIC_UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")
val MOWER_WRITE_CHARACTERISTIC_UUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb")

val CHARACTERISTIC_PERMISSION_READ = BluetoothGattCharacteristic.PERMISSION_READ
val CHARACTERISTIC_PERMISSION_WRITE = BluetoothGattCharacteristic.PERMISSION_WRITE

val STATE_NONE = 0          // we're doing nothing
val STATE_LISTEN = 1        // now listening for incoming connectings
val STATE_CONNECTING = 2    // now initiating an outgoing connecting
val STATE_CONNECTED = 3     // now connected to a remote device

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

val ACTION_READY_TO_CONNECT = "6"

val LIGHT_ACK = "L"
val POS_EVENT_ACK = "0"
val COLL_EVENT_ACK = "1"

val BLUETOOTH_SCAN_PERIOD: Long = 10000
