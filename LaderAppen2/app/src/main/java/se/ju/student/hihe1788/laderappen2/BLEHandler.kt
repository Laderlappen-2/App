package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

private const val SCAN_PERIOD: Long = 10000

class BLEHandler (private val mMainActivity: MainActivity) {

    private var mBluetoothManager: BluetoothManager = mMainActivity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    /*private val mBluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        mBluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothManager.adapter
    }*/
    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var mScanning: Boolean = false
    private lateinit var mDevice: BluetoothDevice


    fun isSupportingBLE() : Boolean
    {
        if ( mMainActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) )
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
            AlertDialog.createSimpleDialog(mMainActivity, mMainActivity.getString(R.string.Bluetooth),
                mMainActivity.getString(R.string.btNotSupported))
            return
        }

        if (mBluetoothAdapter.isEnabled) {
            BluetoothHandler.mBluetoothAdapter.disable()
        } else {
            BluetoothHandler.mBluetoothAdapter.enable()
        }
    }

    fun requestBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        mMainActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    fun connectTo(device: BLEDevice) {

    }

    //private var mHandler: Handler = Handler() {}

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
