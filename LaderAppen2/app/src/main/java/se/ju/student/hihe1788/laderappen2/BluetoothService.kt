package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2

class BluetoothService(private val handler: Handler) {

    // ConnectThread
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        public override fun run() {
            bluetoothAdapter?.cancelDiscovery()

            socket?.use { socket ->
                socket.connect()

                // Perhaps initBluetoothService(handler, socket)
                // start initConnectedThread(socket)
            }
        }

        fun cancel() {
            try {
                socket?.close()
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