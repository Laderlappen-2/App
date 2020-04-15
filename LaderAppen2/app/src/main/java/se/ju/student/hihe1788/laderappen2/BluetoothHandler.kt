package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.os.Handler

object BluetoothHandler {
    //btadapter
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    // all the functions, ya know: connect to bt, enable bt osv

    fun isBluetoothEnabled(): Boolean {
        if (mBluetoothAdapter?.isEnabled == false) {
            return false
        }
        return true
    }

    fun enableBluetooth() {
        if (mBluetoothAdapter == null) {
            // device does not support Bluetooth -> send msg to user
            //TODO give user alertDialog
        }

        if (mBluetoothAdapter?.isEnabled == false) {
            mBluetoothAdapter.enable()
        } else {
            mBluetoothAdapter?.disable()
        }
    }

}