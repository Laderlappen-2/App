package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothDevice
import java.util.*

object BLEDevice {
    private lateinit var mBluetoothDevice: BluetoothDevice
    private var mUUID: UUID = UUID.randomUUID()
    val autoConnect: Boolean = false

    fun setDevice(device: BluetoothDevice) { mBluetoothDevice = device }

    fun getDevice() : BluetoothDevice { return mBluetoothDevice }

    fun getAddress() : String { return mBluetoothDevice.address }

    fun getName() : String { return mBluetoothDevice.name }

    fun setUUID(uuid: UUID) { mUUID = uuid }

    fun getUUID() : UUID { return mUUID }

}