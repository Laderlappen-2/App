package se.ju.student.hihe1788.laderappen2.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Shared preferences manager to enable saving stuff, such as app settings
 * @property context The application context to work with
 */
class SharedPreferencesManager(context: Context) {
    private val PREFERENCES_NAME = "app_settings"
    private val DEVICE_LIST = "devices"
    private val STEERING_MODE = "steeringMode"
    private val DEFAULT_STEERING_MODE = SteeringModeEnum.TWO_HANDED

    private var sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Update the saved steering mode
     * @param steeringMode The steering mode to save
     */
    fun setSteeringMode(steeringMode: SteeringModeEnum) {
        sharedPrefs.edit {
            this.putInt(STEERING_MODE, steeringMode.value)
            this.commit()
        }
    }

    /**
     * Returns the saved steering mode. If no steering mode is saved, it defaults to TWO_HANDED.
     * @return The saved steering mode
     */
    fun getSteeringMode(): SteeringModeEnum {
        var steeringMode = SteeringModeEnum.fromValue(sharedPrefs.getInt(STEERING_MODE, DEFAULT_STEERING_MODE.value))
        steeringMode?.let {
            return it
        }
        return DEFAULT_STEERING_MODE
    }

    /**
     * Get all stored devices
     * @return A set of stored device mac addresses
     */
    fun getDevices(): MutableSet<String> {
        val deviceList = sharedPrefs.getStringSet(DEVICE_LIST, HashSet<String>())
        deviceList?.let {
            return deviceList
        }
        return HashSet()
    }

    /**
     * Sets the saved devices set
     * @param devices The list of devices to save
     */
    fun setDevices(devices: Set<String>) {
        sharedPrefs.edit {
            this.putStringSet(DEVICE_LIST, devices)
            this.commit()
        }
    }

    /**
     * Adds a device to the current list of stored devices
     * @param device The mac address of the device to save
     */
    fun addDevice(device: String) {
        val devices = this.getDevices()
        devices.add(device)
        this.setDevices(devices)
    }

    /**
     * Removes a device from the current list of stored devices
     * @param device The mac address of the device to remove
     */
    fun removeDevice(device: String) {
        val devices = this.getDevices()
        devices.remove(device)
        this.setDevices(devices)
    }
}