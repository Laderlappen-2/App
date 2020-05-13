package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Shows the settings view and its awesome customization features.
 */
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
        setUI()

        view.findViewById<ImageButton>(R.id.bluetooth_btn)?.setOnClickListener {

            it.isSelected = !BLEHandler.isBluetoothEnabled()
            BLEHandler.toggleBluetooth()

        }

        view.findViewById<ImageButton>(R.id.device_connected)?.setOnClickListener {

        it.isSelected = !MowerModel.isConnected && BLEHandler.isBluetoothEnabled()

        Toast.makeText(context, "BUTTON CLICKED!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        setUI()
    }

    /**
     * Setups the UI based on states in the application.
     */
    private fun setUI() {
        if (MowerModel.isConnected) {
            view?.findViewById<ImageButton>(R.id.device_connected)?.isSelected = true
        }
        view?.findViewById<ImageButton>(R.id.bluetooth_btn)?.isSelected = BLEHandler.isBluetoothEnabled()
    }
}