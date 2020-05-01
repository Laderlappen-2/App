package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import java.util.logging.Handler

private const val SCAN_PERIOD: Long = 10000

class BLEHandler (private val mContext: Context) {

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mHandler: Handler
    private var mScanning: Boolean = false
    private lateinit var mDevice: BluetoothDevice


    fun isSupportingBLE() : Boolean
    {
        if ( mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) )
            return true
        return false
    }
}