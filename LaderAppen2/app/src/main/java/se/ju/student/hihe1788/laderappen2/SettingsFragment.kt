package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //DO STUFF THAT WE WANT TO DO HERE :D

        view.findViewById<ImageButton>(R.id.bluetooth_btn_on)?.setOnClickListener {
            if (!BluetoothHandler.isBluetoothEnabled()) {
                BluetoothHandler.enableBluetooth()
                Toast.makeText(context, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<ImageButton>(R.id.bluetooth_btn_off)?.setOnClickListener {
            if (BluetoothHandler.isBluetoothEnabled()) {
                BluetoothHandler.enableBluetooth()
                Toast.makeText(context, "Bluetooth disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}