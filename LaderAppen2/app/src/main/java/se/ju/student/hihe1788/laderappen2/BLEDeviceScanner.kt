package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.os.Looper
import java.util.logging.Handler

class BLEDeviceScanner{

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var IsScanning = false
    //private val mHandler: Handler = object : Handler {}

    private fun scanLEDevices(enable: Boolean)
    {
        when(enable)
        {
            true -> {
                //mHandler.postDelayed
            }
            else -> {
                //mBluetoothAdapter.stopLeScan()
            }
        }
    }

    fun isScanning() : Boolean {
        return IsScanning
    }

    fun start() {

    }

}