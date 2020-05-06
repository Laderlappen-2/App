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

    //High level manager used to obtain an instance of an BluetoothAdapter and to conduct overall Bluetooth Management.
    var mBluetoothManager: BluetoothManager? = null;

    //There's one Bluetooth adapter for the entire system, and your application can interact with it using this object.
    var mBluetoothAdapter: BluetoothAdapter? = null

    //MAC Address of the connected BLE device
    var mDeviceAddress: String? = null

    //Used to conduct GATT client operations
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

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            if(isConnected()) {
                // DO STUFF HERE
            } else {
                init()
            }
        }

        val success = mHandler.postDelayed(mRunnable, 2000)
        //Line 38 needs to be recalled as soon as the previous call has been finished
        //This is going to be the function that will loop and check for new data in the bluetooth stream

        return START_STICKY
    }

    // MainActivity.stopService(intent)
    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    private fun isConnected () : Boolean {
        return mIsConnected;
    }

    // Initialize BluetoothAdapter from BLuetoothManager
    private fun init() : Boolean
    {
        Log.i(TAG, "init()")
        if(mBluetoothManager == null) {
            mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if(mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BLuetoothManager" )
                return false
            }
        }

        mBluetoothAdapter = mBluetoothManager!!.adapter
        if(mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to get initialize BluetoothAdapter")
            return false
        }
        connect(MOWER_MAC_ADDRESS)
        return true
    }

    private fun isEnabled(): Boolean {
        return this.mBluetoothAdapter!!.isEnabled
    }

    private fun connect(deviceAddress: String?) : Boolean {
        val device = mBluetoothAdapter!!.getRemoteDevice(deviceAddress)

        if(device == null || mBluetoothAdapter == null) {
            Log.e(TAG, "Cannot connect to device")
            return false
        }

        Log.i(TAG,"Device object: " + device.toString())

        mDeviceAddress = deviceAddress;
        mGatt = device.connectGatt(this, false, gattCallback)

        Log.i(TAG,"GATT_SUCCES code : " +BluetoothGatt.GATT_SUCCESS.toString());


        if(mGatt != null) {
            Log.i(TAG,"Services in connect: " + mGatt!!.services)
        }

        mIsConnected = true;
        Log.i(TAG, "We are connnected to Gatt server on Doris")
        return true
    }

    //Used to deliver results to the client, such as connection status, as well as any further GATT client operations.
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

                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED-> {
                            //broadcastUpdate(ACTION_GATT_CONNECTED)
                            mGatt?.discoverServices()
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                            broadcastUpdate(ACTION_GATT_DISCONNECTED)
                        }
                    }

                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int)
        {
            super.onServicesDiscovered(gatt, status)
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {

                    // Get the counter characteristic
                    val characteristic = gatt?.getService(MOWER_SERVICE_UUID)?.getCharacteristic(
                        MOWER_CHARACTERISTIC_READ_UUID)

                    // Enable notifications for this characteristic locally
                    gatt?.setCharacteristicNotification(characteristic, true)

                    // Write on the config descriptor to be notified when the value changes
                    val descriptor = characteristic?.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
                    descriptor?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt?.writeDescriptor(descriptor);


                    /*
                    mGattService = gatt?.getService(MOWER_SERVICE_UUID)
                    Log.i(TAG, mGattService.toString())

                    val writeCharacteristic = findCharacteristicsFromDevice(MOWER_MAC_ADDRESS, MOWER_CHARACTERISTIC_WRITE_UUID)
                    if(writeCharacteristic == null)
                    {
                        Log.e(TAG, "$writeCharacteristic is null")
                    } else {
                        Log.i(TAG, "THis is characteristic:  $writeCharacteristic")
                        mGattCharacteristicWrite = writeCharacteristic
                    }

                    val readCharacteristic = findCharacteristicsFromDevice(MOWER_MAC_ADDRESS, MOWER_CHARACTERISTIC_READ_UUID)
                    if(readCharacteristic == null)
                    {
                        Log.e(TAG, "$readCharacteristic is null")
                        return
                    }
                    mGattCharacteristicRead = readCharacteristic

                    gatt?.setCharacteristicNotification(readCharacteristic, true)
                    val descriptor = readCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    val success = gatt?.writeDescriptor(descriptor)
                    */
                }
                else -> {
                    Log.w(TAG, "onServicesdiscovered: " + status)
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

                    if (MOWER_CHARACTERISTIC_READ_UUID.equals(characteristic.uuid))
                    {
                        val data = characteristic.value
                        val value = data.toString()
                        Log.i(TAG, "Successfully read from characteristics: $characteristic"+ "value: " + value)
                    }
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
                    //TODO say we have written data
                    Log.i(TAG, "Data written $characteristic")
                    Thread.sleep(1000)
                    //TODO broadcast intent that says we have written data
                    broadcastUpdate(ACTION_DATA_WRITTEN, characteristic)
                }
                else -> {
                    Log.i(TAG, "onCharacteristicWrite: FAIL")
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
                val value = data.toString()
            }

            Log.i(TAG, "WE ARE in charactersistici channnnnnnngeeeed:  " + "Value read: " + characteristic!!.value.toString())
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            if (CLIENT_CHARACTERISTIC_CONFIG.equals(descriptor?.uuid)) {
                val characteristic = gatt?.getService(MOWER_SERVICE_UUID)?.getCharacteristic(MOWER_CHARACTERISTIC_READ_UUID);
                gatt?.readCharacteristic(characteristic)

                Log.i(TAG, "onDescriptorWrite()")
            }
        }
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

    fun findCharacteristicsFromDevice(Mac_address: String, characteristicUUID: UUID) : BluetoothGattCharacteristic? {
        Log.i(TAG, "I AM IN FIND CHAR")

        if(mGatt == null) {
            Log.e(TAG, "BLuetoothgatt is null")
            return null;
        }

        Log.i(TAG, mGatt!!.services.toString())

        for(service in mGatt!!.services) {
            val characteristic : BluetoothGattCharacteristic? = service!!.getCharacteristic(characteristicUUID)
            if(characteristic != null) {
                Log.i(TAG, "CHARACTERISTIC : " + characteristic.toString())
                return characteristic
            }
        }
        return null;

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

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }

    fun getDescriptorUUID(bluetoothGattCharacteristic: BluetoothGattCharacteristic) {
        val descriptors = bluetoothGattCharacteristic.descriptors
        for (descriptor in descriptors)
        {
            Log.i(TAG, "Descriptors in given characteristic: " + descriptor.uuid.toString())
        }
    }


    //Test functions to check if the service correctly receives and sends data
    fun testInData(data: String){
        //toast("received: $data")
    }

    fun testOutData(): String {
        return("Hej från service")
    }

    fun sendCommand(input: String) {

    }
    fun disconnect() {

    }

    inner class MyLocalBinder : Binder() {
        fun getService() : BLEService {
            return this@BLEService
        }
    }
}