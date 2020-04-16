package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Handler
import android.os.Message

object BluetoothHandler {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mBluetoothService: BluetoothService

    private var mHandler = @SuppressLint("HandlerLeak")
    object: Handler() {

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
                Constants.MESSAGE_DEVICE_NAME -> {
                    /* Save the connected device's name */
                    println("We have the connected device's name")
                }
                Constants.MESSAGE_TOAST -> {
                    /* Receives e.g Connection failed/lost. */
                    val m = msg.data.getString(Constants.TOAST)
                    intent.action = Constants.ACTION_ALERT
                    intent.putExtra("message", m)

                }
            }
            MainActivity.appContext.sendBroadcast(intent)

        }
    }

    // all the functions, ya know: connect to bt, enable bt osv

    fun isBluetoothEnabled(): Boolean {
        if (mBluetoothAdapter?.isEnabled == false) {
            return false
        }
        return true
    }

    fun toggleBluetooth() {
        if (mBluetoothAdapter == null) {
            AlertDialog.createSimpleDialog(MainActivity.appContext, "Bluetooth",
                "Your device does not support Bluetooth.")
        }

        if (mBluetoothAdapter?.isEnabled == false) {
            mBluetoothAdapter.enable()
        } else {
            mBluetoothAdapter?.disable()
        }
    }

    fun connectDevice() {
        val device = mBluetoothAdapter.getRemoteDevice(MowerModel.address)
        mBluetoothService = BluetoothService(mHandler, MainActivity.appContext)

        mBluetoothService.connect(device)
    }

    fun sendMsg(bytes: ByteArray) {
        mBluetoothService.write(bytes)
    }
}