package se.ju.student.hihe1788.laderappen2

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val SCAN_PERIOD: Long = 10000

private val TAG = BLEService::class.java.simpleName
private const val STATE_DISCONNECTED = 0
//private const val STATE_CONNECTING = 1
//private const val STATE_CONNECTED = 2
const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
const val ACTION_GATT_SERVICES_DISCOVERED =
    "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
const val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
//val UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT)

class BLEHandler (private val mContext: Context) {

    private var mBluetoothManager: BluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    /*private val mBluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        mBluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothManager.adapter
    }*/
    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mBluetoothGatt: BluetoothGatt
    private var mScanning: Boolean = false
    private lateinit var mDevice: BluetoothDevice
    private var mGattCharacteristics: MutableList<BluetoothGattCharacteristic> = mutableListOf()
    private lateinit var mGattCharacteristicRead: BluetoothGattCharacteristic
    private lateinit var mGattCharacteristicWrite: BluetoothGattCharacteristic
    private var enabled: Boolean = true


    fun isSupportingBLE() : Boolean
    {
        if ( mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) )
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
        if (BluetoothHandler.mBluetoothAdapter == null) {
            AlertDialog.createSimpleDialog(mContext, mContext.getString(R.string.Bluetooth),
                mContext.getString(R.string.btNotSupported))
            return
        }

        if (mBluetoothAdapter.isEnabled) {
            BluetoothHandler.mBluetoothAdapter.disable()
        } else {
            BluetoothHandler.mBluetoothAdapter.enable()
        }
    }

    fun requestBluetooth() {
        //val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        //mContext.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    fun connectTo(device: BLEDevice) {
        mBluetoothGatt = device.getDevice().connectGatt(mContext, device.autoConnect, gattCallback)
        println("WE HAVE A GATT")
    }

    //private var mHandler: Handler = Handler() {}

    private var connectionState = STATE_DISCONNECTED

    // Various callback methods defined by the BLE API.
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            val intentAction: String
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    intentAction = ACTION_GATT_CONNECTED
                    connectionState = STATE_CONNECTED
                    println("BLEHandler: gattCallback: onConnectionStateChange: newState == STATE_CONNECTED")
                    mBluetoothGatt.discoverServices()

                    //broadcastUpdate(intentAction)

                    //Log.i(TAG, "Connected to GATT server.")
                    //Log.i(TAG, "Attempting to start service discovery: " + bluetoothGatt?.discoverServices())
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    intentAction = ACTION_GATT_DISCONNECTED
                    connectionState = STATE_DISCONNECTED
                    //Log.i(TAG, "Disconnected from GATT server.")
                    //broadcastUpdate(intentAction)
                    println("BLEHandler: gattCallback: onConnectionStateChange: newState == STATE_DISCONNECTED")
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            println("BLEHandler: gattCallback: onServicesDiscovered: GATT_SUCCESS")

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                }
                else -> {
                    Log.w(TAG, "onServicesDiscovered received: $status")
                }
            }

            /*
            mGattCharacteristicRead = gatt.getService(MOWER_SERVICE_UUID)
                .getCharacteristic(MOWER_READ_CHARACTERISTIC_UUID)

            mGattCharacteristicRead.descriptors.forEach { descriptor ->
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                //mBluetoothGatt.writeDescriptor(descriptor)
                mBluetoothGatt.readDescriptor(descriptor)
            }
            mBluetoothGatt.setCharacteristicNotification(mGattCharacteristicRead, enabled)
             */
            /*
            mGattCharacteristicRead = gatt.getService(MOWER_SERVICE_UUID)
                .getCharacteristic(MOWER_READ_CHARACTERISTIC_UUID)

            mGattCharacteristicWrite = gatt.getService(MOWER_SERVICE_UUID)
                .getCharacteristic(MOWER_WRITE_CHARACTERISTIC_UUID)

            val descriptorWrite = mGattCharacteristicWrite.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG).apply {
                value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            }

            val descriptorRead = mGattCharacteristicRead.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG).apply {
                value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            }

            gatt.writeDescriptor(descriptorWrite)
            gatt.readDescriptor(descriptorRead)

            val success = gatt.setCharacteristicNotification(mGattCharacteristicRead, enabled)

            if (success)
                println("setCharacteristicNotification == SUCCESS")

            */

            /*when (status) {
                BluetoothGatt.GATT_SUCCESS -> broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                else -> //Log.w(TAG, "onServicesDiscovered received: $status")
            }*/
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            println("BLEHandler: gattCallback: onCharacteristicRead()")
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    println("BLEHandler: gattCallback: onCharacteristicRead(): GATT_SUCCESS")
                    //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            println("BLEHandler: gattCallback: onCharacteristicChanged(): WE RECEIVED DATA FROM MOWER")
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic.value)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            println("BLEHandler: gattCallback: onCharacteristicWrite()")
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            println("BLEHandler: gattCallback: onDescriptorRead()")

            val success = gatt?.readCharacteristic(mGattCharacteristicRead)
            if(success!!)
                println("gatt?.readCharacteristic(characteristic): SUCCEEDED")
            else
                println("gatt?.readCharacteristic(characteristic): NOT SUCCESS!!!!")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            println("BLEHandler: gattCallback: onDescriptorWrite()")

            //val bArr = byteArrayOf(1,1)
            //mGattCharacteristicWrite.value = bArr
            //gatt?.writeCharacteristic(mGattCharacteristicWrite)
        }

    }

    fun getSupportedGattServices() : List<BluetoothGattService>
    {
        return mBluetoothGatt.services
    }

    fun getBluetoothGatt() : BluetoothGatt
    {
        return mBluetoothGatt
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent()
        intent.setAction(action)
        mContext.sendBroadcast(intent)
    }



    /**
    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        when (characteristic.uuid) {
            UUID_HEART_RATE_MEASUREMENT -> {
                val flag = characteristic.properties
                val format = when (flag and 0x01) {
                    0x01 -> {
                        Log.d(TAG, "Heart rate format UINT16.")
                        BluetoothGattCharacteristic.FORMAT_UINT16
                    }
                    else -> {
                        Log.d(TAG, "Heart rate format UINT8.")
                        BluetoothGattCharacteristic.FORMAT_UINT8
                    }
                }
                val heartRate = characteristic.getIntValue(format, 1)
                Log.d(TAG, String.format("Received heart rate: %d", heartRate))
                intent.putExtra(EXTRA_DATA, (heartRate).toString())
            }
            else -> {
                // For all other profiles, writes the data formatted in HEX.
                val data: ByteArray? = characteristic.value
                if (data?.isNotEmpty() == true) {
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                    intent.putExtra(EXTRA_DATA, "$data\n$hexString")
                }
            }

        }
        sendBroadcast(intent)
    }
    */

}
    /*
    private var mHandler = @SuppressLint("HandlerLeak")
    object: Handler() {

        /**
         * Receives messages from BluetoothService and creates intents so
         * that the BroadcastReceiver can pick them up.
         * @param msg A Message that consists of what the message is about and some data.
         */
        override fun handleMessage(msg: Message) {
            val intent = Intent()
            when(msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> {
                    /* Send to Broadcast Receiver */
                    println("State changed")
                    when (msg.arg1) {
                        Constants.STATE_CONNECTED -> {
                            intent.action = Constants.ACTION_STATE_CONNECTED
                            println("State = CONNECTED")
                        }
                        Constants.STATE_CONNECTING -> {
                            intent.action = Constants.ACTION_STATE_CONNECTING
                            println("State = CONNECTING")
                        }
                        Constants.STATE_LISTEN -> {
                            intent.action = Constants.ACTION_STATE_LISTEN
                            println("State = LISTEN")
                        }
                        Constants.STATE_NONE -> {
                            intent.action = Constants.ACTION_STATE_NONE
                            println("State = NONE")
                        }
                    }
                }
                Constants.MESSAGE_WRITE -> {
                    println("msg sent to mower")
                }
                Constants.MESSAGE_READ -> {
                    /* Received a message from Mower */
                    println("We have a received a message")
                    val data = msg.obj as ByteArray
                    intent.action = Constants.ACTION_MSG_RECEIVED
                    intent.putExtra("message", data)
                }
                Constants.MESSAGE_TOAST -> {
                    /* Receives e.g Connection failed/lost. */
                    val m = msg.data.getString(Constants.TOAST)
                    intent.action = Constants.ACTION_ALERT
                    intent.putExtra("message", m)

                }
            }
            MainActivity.mAppContext.sendBroadcast(intent)

        }
    }
     */
