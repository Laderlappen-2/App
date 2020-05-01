package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BTStateReceiver (private val mContext: Context) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
        {
            val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

            when (state)
            {
                BluetoothAdapter.STATE_TURNING_OFF -> {
                    /**
                     * BLUETOOTH IS TURNING OFF
                     * - Automatically switch back on
                     */
                }

                BluetoothAdapter.STATE_OFF -> {
                    /**
                     * BLUETOOTH IS OFF
                     * - Inform user
                     * - Give opportunity to switch on
                     */
                }

                BluetoothAdapter.STATE_TURNING_ON -> {
                    /**
                     * BLUETOOTH IS TURNING ON
                     */
                }

                BluetoothAdapter.STATE_ON -> {
                    /**
                     * BLUETOOTH IS ON
                     */
                }

                BluetoothAdapter.STATE_CONNECTING -> {
                    /**
                     * IS IN CONNECTING STATE
                     */
                }

                BluetoothAdapter.STATE_CONNECTED -> {
                    /**
                     * IS IN CONNECTED STATE
                     */
                }

                BluetoothAdapter.STATE_DISCONNECTING -> {
                    /**
                     * IS IN DISCONNECTING STATE
                     */
                }

                BluetoothAdapter.STATE_DISCONNECTED -> {
                    /**
                     * IS IN DISCONNECTED STATE
                     */
                }
            }

        }

    }
}