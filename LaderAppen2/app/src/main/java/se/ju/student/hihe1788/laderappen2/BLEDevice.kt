package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothDevice

class BLEDevice(private val mBluetoothDevice: BluetoothDevice) {
    private var mRSSI: Int = 0

    fun getAddress() : String { return mBluetoothDevice.address }

    fun getName() : String { return mBluetoothDevice.name }

    fun setRSSI(rssi: Int) { mRSSI = rssi }

    fun getRSSI() : Int { return mRSSI }

}