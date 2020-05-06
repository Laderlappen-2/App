package se.ju.student.hihe1788.laderappen2.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {
    private val PREFERENCES_NAME = "app_settings"
    private val DEVICE_LIST = "devices"

    private var sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getDevices(): MutableSet<String> {
        val deviceList = sharedPrefs.getStringSet(DEVICE_LIST, HashSet<String>())
        deviceList?.let {
            return deviceList
        }
        return HashSet()
    }

    fun setDevices(devices: Set<String>) {
        sharedPrefs.edit {
            this.putStringSet(DEVICE_LIST, devices)
            this.commit()
        }
    }

    fun addDevice(device: String) {
        val devices = this.getDevices()
        devices.add(device)
        this.setDevices(devices)
    }
}