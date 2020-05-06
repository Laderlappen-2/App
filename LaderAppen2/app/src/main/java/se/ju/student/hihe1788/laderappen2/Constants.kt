package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

/**
 * A place store all constants.
 */
val MOWER_MAC_ADDRESS = "00:1B:10:65:FC:81"
val MOWER_SERVICE_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
var MOWER_SERVICE_ALTERNATIVE_UUID: UUID = UUID.fromString("9e5d1e47-5c13-43a0-8635-82ad38a1386f")
val MOWER_CHARACTERISTIC_READ_UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")
val MOWER_CHARACTERISTIC_WRITE_UUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb")
val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
val APP_UUID = UUID.fromString("0a1bb95f-c32e-4718-92a8-0b88c25b1035")
//c83c7113-5ed0-4839-b512-7bb65a38f627

val STATE_DISCONNECTED = 0
//private const val STATE_CONNECTING = 1
//private const val STATE_CONNECTED = 2
const val ACTION_GATT_CONNECTED = BuildConfig.APPLICATION_ID+".ACTION_GATT_CONNECTED"
const val ACTION_GATT_DISCONNECTED = BuildConfig.APPLICATION_ID+".ACTION_GATT_DISCONNECTED"
const val ACTION_GATT_SERVICES_DISCOVERED = BuildConfig.APPLICATION_ID+".ACTION_GATT_SERVICES_DISCOVERED"
const val ACTION_GATT_DATA_AVAILABLE = BuildConfig.APPLICATION_ID+".ACTION_DATA_AVAILABLE"
const val ACTION_DATA_WRITTEN = BuildConfig.APPLICATION_ID+".ACTION_DATA_WRITTEN"
const val ACTION_GATT_REGISTER_CHARACTERISTIC_READ = BuildConfig.APPLICATION_ID+".ACTION_GATT_REGISTER_CHARACTERISTIC_READ"
const val GATT_EXTRA_DATA = BuildConfig.APPLICATION_ID+".GATT_EXTRA_DATA"

const val BLUETOOTH_REQUEST_ENABLE = 1

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

val ON = 1
val OFF = 0





