package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import java.util.*

object BLEDevice {
    private var mBluetoothDevice: BluetoothDevice
    private var mUUID: UUID = UUID.randomUUID()
    val autoConnect: Boolean = true
    val address = "00:1B:10:65:FC:81"

    init {
        mBluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address)
    }

    fun setDevice(device: BluetoothDevice) { mBluetoothDevice = device }

    fun getDevice() : BluetoothDevice { return mBluetoothDevice }

    fun address() : String { return address }

    fun getName() : String { return mBluetoothDevice.name }

    fun setUUID(uuid: UUID) { mUUID = uuid }

    fun getUUID() : UUID { return mUUID }

}
