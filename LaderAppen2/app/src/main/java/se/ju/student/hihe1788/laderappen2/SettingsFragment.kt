package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import se.ju.student.hihe1788.laderappen2.util.SharedPreferencesManager
import se.ju.student.hihe1788.laderappen2.util.SteeringModeEnum

/**
 * Shows the settings view and its awesome customization features.
 */
class SettingsFragment : Fragment() {
    lateinit var settings: SharedPreferencesManager

    /**
     * Override function that returns the view for SettingsFragment.
     * @see R.layout.settings_fragment
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    /**
     * Override function that calls setUI() and sets on click listeners to the buttons in the view.
     * @see setUI
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings = SharedPreferencesManager(view.context)

        setUI()

        view.findViewById<ImageButton>(R.id.bluetooth_btn)?.setOnClickListener {

            it.isSelected = !BLEHandler.isBluetoothEnabled()
            BLEHandler.toggleBluetooth()

        }

        view.findViewById<ImageButton>(R.id.device_connected)?.setOnClickListener {

        it.isSelected = !MowerModel.isConnected && BLEHandler.isBluetoothEnabled()

        Toast.makeText(context, "BUTTON CLICKED!", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageButton>(R.id.steering_btn_left)?.setOnClickListener {
            setSteeringMode(SteeringModeEnum.LEFT_HANDED, it)
        }
        view.findViewById<ImageButton>(R.id.steering_btn_right)?.setOnClickListener {
            setSteeringMode(SteeringModeEnum.RIGHT_HANDED, it)
        }
        view.findViewById<ImageButton>(R.id.steering_btn_one)?.setOnClickListener {
            setSteeringMode(SteeringModeEnum.TWO_HANDED, it)
        }
    }

    /**
     * Sets the steering mode.
     * @param steeringMode The chosen mode
     * @param button The button pressed
     */
    private fun setSteeringMode(steeringMode: SteeringModeEnum, button: View) {
        settings.setSteeringMode(steeringMode)
        resetSteeringButtons()
        button.isSelected = true
    }

    /**
     * Resets the steering mode buttons.
     */
    private fun resetSteeringButtons() {
        view?.findViewById<ImageButton>(R.id.steering_btn_one)?.isSelected = false
        view?.findViewById<ImageButton>(R.id.steering_btn_right)?.isSelected = false
        view?.findViewById<ImageButton>(R.id.steering_btn_left)?.isSelected = false
    }

    /**
     * Override function that calls setUI()
     * @see setUI
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
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

        // Steering mode
        when(settings.getSteeringMode()) {
            SteeringModeEnum.LEFT_HANDED -> {
                view?.findViewById<ImageButton>(R.id.steering_btn_left)?.isSelected = true
            }
            SteeringModeEnum.RIGHT_HANDED -> {
                view?.findViewById<ImageButton>(R.id.steering_btn_right)?.isSelected = true
            }
            SteeringModeEnum.TWO_HANDED -> {
                view?.findViewById<ImageButton>(R.id.steering_btn_one)?.isSelected = true
            }
        }
    }
}