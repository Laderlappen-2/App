package se.ju.student.hihe1788.laderappen2

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.*

private val TAG = BLEService::class.java.simpleName

class BLEService : Service() {

    private var mIsConnected = false

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private val mBinder = MyLocalBinder()

    var mBluetoothManager: BluetoothManager? = null
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mDeviceAddress: String? = null
    var mGatt: BluetoothGatt? = null
    var mGattService: BluetoothGattService? = null
    var mGattCharacteristicWrite: BluetoothGattCharacteristic? = null
    var mGattCharacteristicRead: BluetoothGattCharacteristic? = null

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startSendingInstructions()

        return START_STICKY
    }

    private fun sendMsg(bytes: ByteArray) {
        Log.i(TAG, "InstructionsSender(): sendMSG == SUCCESS")
    }

    private fun startSendingInstructions() {
        mHandler = Handler()
        mHandler.post(InstructionsSender)
    }

    private val InstructionsSender : Runnable = Runnable {
        run {
            if(isConnected()) {
                this.send()
            } else {
                init()
                Log.i(TAG, "InstructionsSender(): init() == SUCCESS")
            }
            mHandler.postDelayed(InstructionsSender, 1000)
        }
    }

    // MainActivity.stopService(intent)
    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
        disconnect()
    }

    private fun isConnected () : Boolean {
        return mIsConnected
    }

    private fun init() : Boolean
    {
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

    private fun connect() : Boolean {
        val device = mBluetoothAdapter!!.getRemoteDevice(MOWER_MAC_ADDRESS)

        if(device == null || mBluetoothAdapter == null) {
            Log.e(TAG, "CANNOT CONNECT TO DEVICE")
            return false
        }

        mDeviceAddress = device.address
        mGatt = device.connectGatt(this, false, gattCallback)

        mIsConnected = true;
        Log.i(TAG,"CONNECTED TO DEVICE: "+device.address)
        return true
    }

    private val gattCallback = object : BluetoothGattCallback()
    {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            super.onConnectionStateChange(gatt, status, newState)

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onConnectionStateChange(): status == GATT_SUCCESS")

                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED-> {
                            //broadcastUpdate(ACTION_GATT_CONNECTED)
                            gatt.discoverServices()
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                            //broadcastUpdate(ACTION_GATT_DISCONNECTED)
                        }
                    }
                }
                else -> {
                    Log.i(TAG, "onConnectionStateChange(): status == GATT_FAIL")
                }
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            Log.i(TAG, "Remote RSSI = $rssi")
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt?,
            status: Int
        ) {
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
                    descriptor?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    gatt?.writeDescriptor(descriptor)
                }
                else -> {
                    Log.i(TAG, "onServicesDiscovered(): status == GATT_FAIL")
                }
            }
        }

        //A request to Read has completed
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            //read data from characteristic.value
            when(status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onCharacteristicRead(): status == GATT_SUCCESS")
                    if (MOWER_CHARACTERISTIC_READ_UUID.equals(characteristic.uuid))
                    {
                        val data = characteristic.value
                        val value = data.toString()
                        Log.i(TAG, "Successfully read from characteristics: $characteristic"+ "value: " + value)
                    }
                }
                else -> {
                    Log.i(TAG, "onCharacteristicRead(): status == GATT_FAIL")
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onCharacteristicWrite(): status == GATT_SUCCESS")
                    Log.i(TAG, "onCharacteristicWrite(): value: " + characteristic.value.toString(Charsets.UTF_8))
                    //Thread.sleep(1000)

                    //broadcastUpdate(ACTION_DATA_WRITTEN, characteristic)
                }
                else -> {
                    Log.i(TAG, "onCharacteristicWrite(): status == GATT_FAIL")
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)

            if (MOWER_CHARACTERISTIC_READ_UUID.equals(characteristic?.uuid))
            {
                val data = characteristic?.value
                val value = data?.toString(Charsets.UTF_8)
                Log.i(TAG, "onCharacteristicChanged():  " + "Value read: " + value)
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onDescriptorWrite(): status == GATT_SUCCESS")
                    if (DESCRIPTOR_UUID.equals(descriptor?.uuid)) {
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

    fun disconnect() {
        mGatt?.disconnect()
    }

    fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        val intent = Intent(action)
        //TODO format the characteristics and send a intent
        val data: ByteArray? = characteristic!!.value
        intent.putExtra(GATT_EXTRA_DATA, "$data")

        sendBroadcast(intent)
    }

    fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    fun send() {
        //check we access to BT radio
        if(mBluetoothAdapter == null || mGatt == null) {
            return
        }
        val bArr = DriveInstructionsModel.toByteArray()
        mGattCharacteristicWrite?.value = bArr

        Log.i(TAG, "send(): " + "bytArray: " + mGattCharacteristicWrite?.value?.toString(Charsets.UTF_8) +" Properties: "+ mGattCharacteristicWrite?.properties?.toInt() +" charValue: " + mGattCharacteristicWrite?.value)

        mGatt!!.writeCharacteristic(mGattCharacteristicWrite)

        //TODO(write to char here?)
    }

    fun getCharThenWrite(command: Int) {
        if(mGattCharacteristicWrite == null) {
            Log.e(TAG, "ERROR in getCharThenWrite")
            return
        }
        writeCharacteristics(mGattCharacteristicWrite!!, command)
    }

    fun readChar() {
        val success = mGatt!!.readCharacteristic(mGattCharacteristicRead)

        if (success)
            Log.i(TAG, "readChar(): SUCCESS")
        else
            Log.i(TAG, "readChar(): FAIL")
    }

    fun writeCharacteristics(characteristic: BluetoothGattCharacteristic, command: Int) {
        //check we access to BT radio
        if(mBluetoothAdapter == null || mGatt == null) {
            return
        }
        var byteArray:ByteArray? = null
        byteArray = byteArrayOf(command.toByte())
        characteristic.value = byteArray

        Log.i(TAG, "I AM IN WRITECHARACTERISTICS: " + "bytaarray: " + byteArray.toString() +"Properties: "+ characteristic.properties.toInt() +" charValue: " + characteristic.value)

        mGatt!!.writeCharacteristic(characteristic)

        //TODO(write to char here?)
    }

    fun getDescriptorUUID(bluetoothGattCharacteristic: BluetoothGattCharacteristic) {
        val descriptors = bluetoothGattCharacteristic.descriptors
        for (descriptor in descriptors)
        {
            Log.i(TAG, "Descriptors in given characteristic: " + descriptor.uuid.toString())
        }
    }

    inner class MyLocalBinder : Binder() {
        fun getService() : BLEService {
            return this@BLEService
        }
    }
}