package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle

import android.os.Handler
import android.os.Message
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothService(var mHandler: Handler, var context: Context) {

    // Debugging
    private val TAG = "BluetoothService"
    private val MY_UUID = UUID.fromString("d42ac549-9825-4b1d-b586-80757bed9788")
    private var mState = Constants.STATE_NONE
    private var mNewState = mState


    @Synchronized
    fun getState(): Int {
        return mState
    }

    @Synchronized
    fun connect(device: BluetoothDevice) {
        if (mState == Constants.STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel()
                mConnectThread = null
            }
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread = null
        }

        mConnectThread = ConnectThread(device)
        mConnectThread.start()

        // TODO Update UI in some elegant way
    }

    @Synchronized
    fun connected(socket: BluetoothSocket, device: BluetoothDevice) {

        if (mConnectThread != null) {
            mConnectThread.cancel()
            mConnectThread = null
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread = null
        }

        mConnectedThread = ConnectedThread(socket)
        mConnectedThread.start()


        val msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME)
        val bundle = Bundle()
        bundle.putString(Constants.DEVICE_NAME, device.name)
        msg.data = bundle
        mHandler.sendMessage(msg)
    }

    @Synchronized
    fun stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel()
            mConnectThread = null
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel()
            mConnectedThread = null
        }

        mState = Constants.STATE_NONE

        // TODO Indicate to user that we are not connected anymore
    }

    fun write(out: ByteArray) {
        val r: ConnectedThread // temporary object

        synchronized(this) {
            if (mState != Constants.STATE_CONNECTED) return
            r = mConnectedThread
        }
        r.write(out) // write unsynchronized
    }

    private fun connectionFail(failMsg: String) {
        val msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString(Constants.TOAST, failMsg)
        msg.data = bundle
        mHandler.sendMessage(msg)

        mState = Constants.STATE_NONE

    }

    // ConnectThread
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        val mmDevice: BluetoothDevice
        init {
            mState = Constants.STATE_CONNECTING
            mmDevice = device
        }

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
        }

        public override fun run() {
            BluetoothHandler.mBluetoothAdapter?.cancelDiscovery()


            mmSocket?.use { socket ->
                try {
                    socket.connect()
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e2: IOException) {
                        println("Unable to close ConnectSocket. Msg: $e2")
                    }

                    connectionFail("Unable to connect to socket from ConnectThread")
                    return
                }
                // Maybe set mConnectThread = null.

                // Start the connected thread
                connected(socket, mmDevice)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                print("ConnectThread could not close. Msg: $e")
            }
        }

    }

    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {

        private val inStream: InputStream = socket.inputStream
        private val outStream: OutputStream = socket.outputStream
        private val buffer: ByteArray = ByteArray(1024)

        override fun run() {
            var numBytes: Int // Amount of bytes returned from read()

            while(true) {
                numBytes = try {
                    inStream.read(buffer)
                } catch (e: IOException) {
                    println("Unable to read from stream. Msg: $e")
                    break
                }

                val readMsg = handler.obtainMessage(MESSAGE_READ, numBytes, -1, buffer)
                readMsg.sendToTarget()

            }
        }

        fun write(bytes: ByteArray) {
            try {
                outStream.write(bytes)
            } catch (e: IOException) {
                println("Error occurred when sending data. Msg: $e")
                /* TODO: Perhaps take care of these errors in a better way.*/
                return
            }

            val writtenMsg = handler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
            writtenMsg.sendToTarget()
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                println("Could not close the connect socket. Msg: $e")
            }
        }
    }
}