package se.ju.student.hihe1788.laderappen2

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log


private val TAG = BLEHandler::class.java.simpleName

object BLEHandler {

    private val mContext = MainActivity.mContext
    private var mBluetoothManager: BluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    /*private val mBluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        mBluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothManager.adapter
    }*/
    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mGatt: BluetoothGatt
    private var mScanning: Boolean = false
    private lateinit var mDevice: BluetoothDevice
    private var mGattCharacteristics: MutableList<BluetoothGattCharacteristic> = mutableListOf()
    private lateinit var mGattCharacteristicRead: BluetoothGattCharacteristic
    private lateinit var mGattCharacteristicWrite: BluetoothGattCharacteristic
    private var enabled: Boolean = true
    private val mBLEService = BLEService()


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
        //mGatt = device.getDevice().connectGatt(mContext, device.autoConnect, gattCallback)
        println("WE HAVE A GATT")
    }

    fun send(input: ByteArray) {
        val msg = "1000".toByteArray()
        mGattCharacteristicWrite.value = input
        mGatt.writeCharacteristic(mGattCharacteristicWrite)
    }

    fun read() {
        mGatt.readCharacteristic(mGattCharacteristicRead)
    }

    fun getSupportedGattServices() : List<BluetoothGattService>
    {
        return mGatt.services
    }

    fun getBluetoothGatt() : BluetoothGatt
    {
        return mGatt
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
