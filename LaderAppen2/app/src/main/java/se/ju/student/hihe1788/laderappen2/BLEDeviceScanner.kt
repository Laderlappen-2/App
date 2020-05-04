package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Message

class BLEDeviceScanner {

    private val mBluetoothScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private var mIsScanning = false
    private var mHandler = @SuppressLint("HandlerLeak")
    object: android.os.Handler() {

        /**
         * Receives messages from BluetoothService and creates intents so
         * that the BroadcastReceiver can pick them up.
         * @param msg A Message that consists of what the message is about and some data.
         */
        override fun handleMessage(msg: Message) {
            val intent = Intent()
            when(msg.what) {
                MESSAGE_STATE_CHANGE -> {
                    /* Send to Broadcast Receiver */
                    println("State changed")
                    when (msg.arg1) {
                        STATE_CONNECTED -> {
                            intent.action = ACTION_STATE_CONNECTED
                            println("State = CONNECTED")
                        }
                        STATE_CONNECTING -> {
                            intent.action = ACTION_STATE_CONNECTING
                            println("BLEDeviceScanner -> Handler -> State = CONNECTING")
                        }
                        STATE_LISTEN -> {
                            intent.action = ACTION_STATE_LISTEN
                            println("State = LISTEN")
                        }
                        STATE_NONE -> {
                            intent.action = ACTION_STATE_NONE
                            println("State = NONE")
                        }
                    }
                }
                MESSAGE_WRITE -> {
                    println("msg sent to mower")
                }
                MESSAGE_READ -> {
                    /* Received a message from Mower */
                    println("We have a received a message")
                    val data = msg.obj as ByteArray
                    intent.action = ACTION_MSG_RECEIVED
                    intent.putExtra("message", data)
                }
                MESSAGE_TOAST -> {
                    /* Receives e.g Connection failed/lost. */
                    val m = msg.data.getString(TOAST)
                    intent.action = ACTION_ALERT
                    intent.putExtra("message", m)

                }
            }
            MainActivity.mContext.sendBroadcast(intent)

        }
    }

    fun isScanning() : Boolean
    {
        return mIsScanning
    }

    fun start()
    {
        println("BLEDeviceScanner onStart()")
        scanBLEDevice(true)
    }

    fun stop()
    {
        scanBLEDevice(false)
    }

    private fun scanBLEDevice(enable: Boolean)
    {
        when(enable && !mIsScanning)
        {
            true -> {
                println("scanBLEDevice enable and not scanning == true")
                mHandler.postDelayed({
                    mIsScanning = false
                    mBluetoothScanner.stopScan(scanCallback)
                }, BLUETOOTH_SCAN_PERIOD)
                mIsScanning = true
                mBluetoothScanner.startScan(scanCallback)
                /**
                 * Another startScan:
                 * mBluetoothScanner.startScan(ScanFilters, ScanSettings, Scancallback)
                 */
            }
            else -> {
                mIsScanning = false
                mBluetoothScanner.stopScan(scanCallback)
            }
        }
    }

    private val scanCallback = object : ScanCallback()
    {
        /** Callback when batch results are delivered. **/
        override fun onBatchScanResults(results: MutableList<ScanResult>?)
        {
            /** results - List of scan results that are previously scanned. **/
            super.onBatchScanResults(results)
        }

        /** Callback when scan could not be started. **/
        override fun onScanFailed(errorCode: Int)
        {
            super.onScanFailed(errorCode)

            when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> {

                }
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> {

                }
                SCAN_FAILED_FEATURE_UNSUPPORTED -> {

                }
                SCAN_FAILED_INTERNAL_ERROR -> {

                }
            }
        }

        /** Callback when a BLE advertisement has been found. **/
        override fun onScanResult(callbackType: Int, result: ScanResult?)
        {
            super.onScanResult(callbackType, result)
            val device = result?.device
            val type = result?.device?.type
            val address = result?.device?.address
            val name = result?.device?.name
            val uuids = result?.device?.uuids

            println("scanCallback: onScanResult: address")

            when(type)
            {
                BluetoothDevice.DEVICE_TYPE_LE ->
                {
                    if (address == MowerModel.address) {
                        mHandler.obtainMessage(STATE_CONNECTING).sendToTarget()
                        val intent = Intent()
                        intent.action = ACTION_READY_TO_CONNECT
                        MainActivity.mContext.sendBroadcast(intent)
                    }
                }
                else -> {

                }
            }

        }
    }
}