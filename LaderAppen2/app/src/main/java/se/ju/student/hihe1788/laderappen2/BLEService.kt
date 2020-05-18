package se.ju.student.hihe1788.laderappen2

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.io.IOException

private val TAG = BLEService::class.java.simpleName

/**
 * A Bluetooth Low Energy service that handles all
 * bluetooth-connectivity for this application.
 */
class BLEService : Service() {

    private var mIsConnected = false

    private lateinit var mHandler: Handler
    private val mBinder = MyLocalBinder()

    var mBluetoothManager: BluetoothManager? = null
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mDeviceAddress: String? = null
    var mGatt: BluetoothGatt? = null
    var mGattCharacteristicWrite: BluetoothGattCharacteristic? = null
    var mGattCharacteristicRead: BluetoothGattCharacteristic? = null

    /**
     * Override function that returns the binded object
     * that allows [MainActivity] to access this class.
     * @param p0: A intent that is needed
     */
    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    /**
     * Override function which runs when the service is about to start.
     * @param intent: Allow [MainActivity] to bind to it.
     * @param flags: Additional data about this start request
     * @param startId: A unique Int representing this specific request to start.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startSendingInstructions()

        return START_STICKY
    }

    /**
     * Schedules a steady stream of [DriveInstructionsModel] to be sent
     * to the connected device (in our case, the mower)
     */
    private fun startSendingInstructions() {
        mHandler = Handler()
        mHandler.post(instructionsSender)
    }

    /**
     * A runnable that keeps sending [DriveInstructionsModel] to the mower
     * at an interval of 250ms.
     */
    private val instructionsSender : Runnable = Runnable {
        run {
            if(mIsConnected) {
                this.send()
            } else {
                init()
            }
            mHandler.postDelayed(instructionsSender, 250)
        }
    }

    /**
     * A lifecycle function that runs when this service is
     * about to be destroyed. Hence the name [onDestroy].
     */
    override fun onDestroy() {
        Log.i(TAG, "onDestroy called")
        super.onDestroy()
        mHandler.removeCallbacks(instructionsSender)
        disconnect()
        stopSelf()
    }


    /**
     * Ensures that the device has support for BLE and
     * then tries to connect.
     * @return true if the device has support and can [connect].
     */
    private fun init() : Boolean {
        if(mBluetoothManager == null) {
            mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if(mBluetoothManager == null) {
                Log.e(TAG, "UNABLE TO INITIALIZE BLUETOOTHMANAGER" )
                return false
            }
        }

        mBluetoothAdapter = mBluetoothManager!!.adapter
        if(mBluetoothAdapter == null) {
            Log.e(TAG, "UNABLE TO INITIALIZE BLUETOOTHADAPTER")
            return false
        }
        connect()
        return true
    }

    /**
     * Connects to the given [MowerModel.address] and returns if it succeeded or not.
     * @return if the connecting was successful
     */
    private fun connect() : Boolean {
        val device = mBluetoothAdapter!!.getRemoteDevice(MowerModel.address)

        if(device == null || mBluetoothAdapter == null) {
            Log.e(TAG, "connect() - CANNOT CONNECT TO DEVICE")
            return false
        }

        mDeviceAddress = device.address
        mGatt = device.connectGatt(this, false, gattCallback)

        mIsConnected = true
        MowerModel.isConnected = true
        Log.i(TAG,"CONNECTED TO DEVICE: ${device.address}")
        return true
    }

    /**
     * Callback-object that is triggered when certain BLE-related events occur.
     */
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            Log.i(TAG, "gattCallback - gatt:  $gatt, status: $status, newState: $newState")

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onConnectionStateChange(): status == GATT_SUCCESS")

                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED-> {
                            gatt.discoverServices()
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                        }
                    }
                }
                else -> {
                    Log.i(TAG, "onConnectionStateChange(): status == GATT_FAIL")
                }
            }
        }

        /**
         * Identifies the connected devices available services.
         * @param gatt: The connected device's gatt profile
         * @param status: Indicates a gatt's status.
         */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onServicesDiscovered(): status == GATT_SUCCESS")
                    mGattCharacteristicRead = gatt?.getService(MOWER_SERVICE_UUID)?.getCharacteristic(
                        MOWER_CHARACTERISTIC_READ_UUID)

                    mGattCharacteristicWrite = gatt?.getService(MOWER_SERVICE_UUID)?.getCharacteristic(
                        MOWER_CHARACTERISTIC_WRITE_UUID)

                    // Enable notifications for this characteristic locally
                    gatt?.setCharacteristicNotification(mGattCharacteristicRead, true)

                    // Write on the config descriptor to be notified when the value changes
                    val descriptor = mGattCharacteristicRead?.getDescriptor(DESCRIPTOR_UUID)
                    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt?.writeDescriptor(descriptor)
                }
                else -> {
                    Log.i(TAG, "onServicesDiscovered(): status == GATT_FAIL")
                }
            }
        }

        /**
         * Override function that is triggered when a [characteristic] is changed.
         * @param gatt: Connected device's gatt profile
         * @param characteristic: The characteristic that has changed
         */
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)

            if (MOWER_CHARACTERISTIC_READ_UUID == characteristic?.uuid) {

                val intent = Intent()
                val data = characteristic?.value
                intent.action = ACTION_DATA_RECEIVED_FROM_MOWER
                intent.putExtra("data", data)

                MainActivity.mContext.sendBroadcast(intent)
                send("A".toByteArray()) // ACK
            }
        }

        /**
         * A callback indicating the result of a descriptor write operation.
         * @param gatt: Connected device's gatt profile
         * @param descriptor: A bluetooth gatt descriptor
         * @param status: The result of the write operation.
         */
        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onDescriptorWrite(): status == GATT_SUCCESS")
                    if (DESCRIPTOR_UUID == descriptor?.uuid) {
                        val characteristic = gatt?.getService(MOWER_SERVICE_UUID)?.getCharacteristic(MOWER_CHARACTERISTIC_READ_UUID)
                        val successCheck = gatt!!.readCharacteristic(characteristic)
                        if (successCheck)
                            Log.i(TAG, "readCharacteristic(characteristic): SUCCESS")
                        else
                            Log.i(TAG, "readCharacteristic(characteristic): FAIL")
                    }
                }
                else -> {
                    Log.i(TAG, "onDescriptorWrite(): status == GATT_FAIL")
                }
            }

        }
    }

    /**
     * Disconnect the [mGatt]
     */
    private fun disconnect() {
        mGatt?.disconnect()
    }

    /**
     * Send a given input [input] to the connected BLE device.
     * Uses [DriveInstructionsModel.toByteArray] as a default parameter since
     * that is the most sent message.
     */
    fun send(input: ByteArray = DriveInstructionsModel.toByteArray()) {
        //check we access to BT radio
        if(mBluetoothAdapter == null || mGatt == null || mGattCharacteristicWrite == null)
            return

        try {
            mGattCharacteristicWrite?.value = input
            mGatt!!.writeCharacteristic(mGattCharacteristicWrite)
        } catch (e: IOException) {
            Log.i(TAG,"Error in send(): $e")
        }
    }


    /**
     * The bind-object that allows [MainActivity] to
     * talk to the [BLEService].
     * @returns [BLEService]
     */
    inner class MyLocalBinder : Binder() {
        fun getService() : BLEService {
            return this@BLEService
        }
    }
}