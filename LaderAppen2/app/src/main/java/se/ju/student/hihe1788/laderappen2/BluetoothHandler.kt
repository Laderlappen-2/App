package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Handler
import android.os.Message
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * An object that handles all Bluetooth stuff.
 */
object BluetoothHandler {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mBluetoothService: BluetoothService
    private lateinit var mDevice: BluetoothDevice

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

    /**
     * Checks if Bluetooth is enabled.
     * @return returns true if Bluetooth is enabled
     */
    fun isBluetoothEnabled(): Boolean {
        if (mBluetoothAdapter?.isEnabled == false) {
            return false
        }
        return true
    }

    /**
     * Toggles Bluetooth on or off. If the device do not support
     * Bluetooth it creates a @see AlertDialog.
     */
    fun toggleBluetooth() {
        if (mBluetoothAdapter == null) {
            AlertDialog.createSimpleDialog(MainActivity.mAppContext, MainActivity.mAppContext.getString(R.string.Bluetooth),
                MainActivity.mAppContext.getString(R.string.btNotSupported))
        }

        if (mBluetoothAdapter?.isEnabled == false) {
            mBluetoothAdapter.enable()
        } else {
            mBluetoothAdapter?.disable()
        }
    }

    /**
     * Initiate BluetoothService.ConnectThread and tries to
     * connect to @see MowerModel.
     */
    fun connectDevice() {
        mDevice = mBluetoothAdapter.getRemoteDevice(MowerModel.address)
        mBluetoothService = BluetoothService(mHandler)

        mBluetoothService.connect(mDevice)
    }

    /**
     * Disconnect the connected device.
     */
    fun disconnectDevice() {
        mBluetoothService.stop()
    }

    /**
     * Send a given message to the connected device.
     * @param bytes: A bytearray of what you would like to send.
     */
    fun sendMsg(bytes: ByteArray) {
        mBluetoothService.write(bytes)
    }

    fun startSendingDriveInstructions() {
        mHandler.post(InstructionsSender)
    }

    private val InstructionsSender : Runnable = Runnable {
        run {
            if (MowerModel.isConnected) {
                this.sendMsg(DriveInstructionsModel.toMessage())
            }
            mHandler.postDelayed(InstructionsSender, 1000)
        }
    }

}