package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BTStateReceiver (private val mContext: Context) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("BTStateReceiver: onReceive()")
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
        else if ( action.equals(ACTION_GATT_CONNECTED) )
        {
            println("BTStateReceiver: onReceive(): ACTION_GATT_CONNECTED")
        }
        else if ( action.equals(ACTION_GATT_SERVICES_DISCOVERED) )
        {
            println("BTStateReceiver: onReceive(): ACTION_GATT_SERVICES_DISCOVERED")

            BLEHandler.getSupportedGattServices().forEach { service ->
                if (service.uuid.equals(MOWER_SERVICE_UUID))
                {
                    val characteristic = service.getCharacteristic(MOWER_READ_CHARACTERISTIC_UUID)
                    /*
                    characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG).apply {
                        value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                    }
                     */

                    characteristic.descriptors.forEach { descriptor ->
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        BLEHandler.getBluetoothGatt().writeDescriptor(descriptor)
                    }
                    BLEHandler.getBluetoothGatt().setCharacteristicNotification(characteristic, true)
                }

            }
        }
    }
}