package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * Handles bluetooth connection and communication via its
 * ConnectThread and ConnectedThread
 * @param mHandler Handles communication outside this class.
 */
class BluetoothService(var mHandler: Handler) {

    //private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
    private var mState = Constants.STATE_NONE
    private lateinit var mConnectThread: ConnectThread
    private lateinit var mConnectedThread: ConnectedThread
    private var mIsConnectThreadExisting = false
    private var mIsConnectedThreadExisting = false

    /**
     * Return the current connection state.
     * @return Current connection state.
     */
    @Synchronized
    fun getState(): Int {
        println("BluetoothService.getState() called")
        return mState
    }

    /**
     * Sets the current connection state.
     * @param state The current connection state.
     */
    @Synchronized
    private fun setState(state: Int) {
        println("BluetoothService.setState() called")
        mState = state
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mState, -1)
            .sendToTarget()
    }

    /**
     * Starts the ConnectThread to initiate a connection to a remote device.
     * @param device The BluetoothDevice to connect to
     */
    @Synchronized
    fun connect(device: BluetoothDevice) {
        println("BluetoothService.connect($device) called")
        if (mState == Constants.STATE_CONNECTING) {
            if (mIsConnectThreadExisting) {
                mConnectThread.cancel()
                mIsConnectThreadExisting = false
            }
        }
        if (mIsConnectedThreadExisting) {
            mConnectedThread.cancel()
            mIsConnectedThreadExisting = false
        }

        mConnectThread = ConnectThread(device)
        mConnectThread.start()
        setState(Constants.STATE_CONNECTING)
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket The BluetoothSocket on which the connection was made.
     */
    @Synchronized
    fun connected(socket: BluetoothSocket) {
        println("BluetoothService.connected($socket) called")
        if (mIsConnectThreadExisting) {
            mConnectThread.cancel()
            mIsConnectThreadExisting = false
        }

        if (mIsConnectedThreadExisting) {
            mConnectedThread.cancel()
            mIsConnectedThreadExisting = false
        }

        mConnectedThread = ConnectedThread(socket)
        mConnectedThread.start()
        setState(Constants.STATE_CONNECTED)
    }

    /**
     * Stop ConnectThread and ConnectedThread
     */
    @Synchronized
    fun stop() {
        println("BluetoothService.stop() called")
        if (mIsConnectThreadExisting) {
            mConnectThread.cancel()
            mIsConnectThreadExisting = false
        }

        if (mIsConnectedThreadExisting) {
            mConnectedThread.cancel()
            mIsConnectedThreadExisting = false
        }

        setState(Constants.STATE_NONE)
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     */
    fun write(out: ByteArray) {
        println("BluetoothService.write($out) called")
        val r: ConnectedThread // temporary object

        synchronized(this) {
            if (mState != Constants.STATE_CONNECTED) return
            r = mConnectedThread
        }
        r.write(out) // write unsynchronized
    }

    /**
     * Indicates that the connection attempt failed or lost and sends a message
     * to the BluetoothHandler.mHandler
     * @see BluetoothHandler.mHandler
     * @param failMsg A message that indicates if it was lost or failed.
     */
    private fun connectionFail(failMsg: String) {
        println("BluetoothService.connectionFail($failMsg) called")
        mHandler.obtainMessage(Constants.MESSAGE_TOAST, failMsg)
            .sendToTarget()

        setState(Constants.STATE_NONE)
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. The connection either succeeds or fails.
     * @param device A BluetoothDevice that you try to connect to.
     */
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        val mmDevice: BluetoothDevice
        init {
            mIsConnectThreadExisting = true
            mmDevice = device
        }

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(MY_UUID)
        }

        /**
         * Represent the lifetime of this thread.
         */
        public override fun run() {
            println("BluetoothService.ConnectThread.run() called")
            if (BluetoothHandler.mBluetoothAdapter.isDiscovering) {
                BluetoothHandler.mBluetoothAdapter?.cancelDiscovery()
            }


            mmSocket?.use { socket ->
                try {
                    socket.connect()
                } catch (e: IOException) {
                    try {
                        socket.close()
                        println("Socket close. Msg: $e")
                    } catch (e2: IOException) {
                        println("Unable to close ConnectSocket. Msg: $e2")
                    }

                    connectionFail(MainActivity.mAppContext.getString(R.string.unableToConnect))
                    return
                }
                connected(socket)
            }
        }

        /**
         * Cancel the ConnectThread
         * @throws IOException
         */
        fun cancel() {
            println("BluetoothService.ConnectThread.cancel() called")
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                println("ConnectThread could not close. Msg: $e")
            }
        }

    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     * @param mmSocket A BluetoothSocket to the remote device.
     * @throws IOException
     */
    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private lateinit var mmInStream: InputStream
        private lateinit var mmOutStream: OutputStream

        init {
            mIsConnectedThreadExisting = true

            try {
                mmInStream = mmSocket.inputStream
                mmOutStream = mmSocket.outputStream

          } catch (e: IOException) {
                println("ConnectedThreadSockets not created. Msg: $e")
            }

        }

        /**
         * Represent the lifetime of this thread.
         */
        override fun run() {
            println("BluetoothService.ConnectedThread.run() called")
            val buffer = ByteArray(1024)
            var numBytes: Int // Amount of bytes returned from read()

            while(mState == Constants.STATE_CONNECTED) {
                try {
                    numBytes = mmInStream.read(buffer)
                    mHandler.obtainMessage(Constants.MESSAGE_READ, numBytes, -1, buffer)
                        .sendToTarget()
                } catch (e: IOException) {
                    println("Unable to read from stream. Msg: $e")
                    connectionFail(MainActivity.mAppContext.getString(R.string.connectionLost))
                    break
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param bytes The bytes to write
         */
        fun write(bytes: ByteArray) {
            println("BluetoothService.ConnectedThread.write($bytes) called")
            try {
                mmOutStream.write(bytes)

                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, bytes)
                    .sendToTarget()
            } catch (e: IOException) {
                println("Error occurred when sending data. Msg: $e")
            }
        }

        /**
         * Cancel this thread.
         * @throws IOException
         */
        fun cancel() {
            println("BluetoothService.ConnectedThread.cancel() called")
            try {
                mmSocket.close()
            } catch (e: IOException) {
                println("Could not close the connect socket. Msg: $e")
            }
        }
    }
}