package se.ju.student.hihe1788.laderappen2

import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager


private val TAG = BLEHandler::class.java.simpleName

object BLEHandler {

    private val mContext = MainActivity.mContext
    private var mBluetoothManager: BluetoothManager =
        mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mGatt: BluetoothGatt
    private var mScanning: Boolean = false
    private lateinit var mDevice: BluetoothDevice
    private var mGattCharacteristics: MutableList<BluetoothGattCharacteristic> = mutableListOf()
    private lateinit var mGattCharacteristicRead: BluetoothGattCharacteristic
    private lateinit var mGattCharacteristicWrite: BluetoothGattCharacteristic
    private var enabled: Boolean = true
    private val mBLEService = BLEService()


    fun isSupportingBLE(): Boolean {
        if (mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            return true
        return false
    }

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     **/
    fun isBluetoothEnabled(): Boolean {
        if (mBluetoothAdapter.isEnabled)
            return true
        return false
    }

    /**
     * Toggles Bluetooth on or off. If the device do not support
     * Bluetooth it creates a @see AlertDialog.
     */
    fun toggleBluetooth() {
        if (mBluetoothAdapter == null) {
            AlertDialog.createSimpleDialog(
                mContext, mContext.getString(R.string.Bluetooth),
                mContext.getString(R.string.btNotSupported)
            )
            return
        }

        if (mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.disable()
        } else {
            mBluetoothAdapter.enable()
        }
    }
}
