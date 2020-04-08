package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter

object BluetoothHandler {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothEnabled(): Boolean {
        if (bluetoothAdapter?.isEnabled == false) {
            return false
        }
        return true
    }

    fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            // device does not support Bluetooth -> send msg to user
        }

        if (bluetoothAdapter?.isEnabled == false) {
            bluetoothAdapter.enable()
        } else {
            bluetoothAdapter?.disable()
        }
    }
}