package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

public class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var discoveredBtDevices: ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var ourMower: BluetoothDevice
    private val MY_UUID = UUID.fromString("c51c605f-63dc-4015-8de5-45525ca7eba0")

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {

                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName= device.name
                    val deviceHardwareAdress = device.address
                    discoveredBtDevices.add(device)

                    if (device.address == "OUR  MAC") {
                        ourMower = device
                        //connect with device
                        connectWithMower()
                    }

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?:return

        val navController : NavController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        /*---------------------[  REGISTER RECEIVERS  ]-----------------------------*/
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    /*---------------------[  SET UP VIEWS AND NAVIGATION  ]-----------------------------*/

    private fun setupActionBar(navController: NavController, appBarconfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarconfig)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    /*---------------------[  BLUETOOTH STUFF  ]-----------------------------*/

    fun isBluetoothEnabled(): Boolean {
        if (bluetoothAdapter?.isEnabled == false) {
            return false
        }
        return true
    }

    fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            // device does not support Bluetooth -> send msg to user
            //TODO give user alertDialog
        }

        if (bluetoothAdapter?.isEnabled == false) {
            bluetoothAdapter.enable()
        } else {
            bluetoothAdapter?.disable()
        }
    }

    fun startBtConnect() {
        // if not bonded with mower
        if (!isPairedWithMower()) {
            bluetoothAdapter?.startDiscovery()
        } else {
            /*bonded*/
            connectWithMower()
        }
    }

    private fun isPairedWithMower(): Boolean {
        val pairedDevices: MutableSet<BluetoothDevice> = bluetoothAdapter?.bondedDevices ?: return false
        return pairedDevices.contains(ourMower)
    }

    private fun connectWithMower() {
        ConnectThread(ourMower).start()
    }

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

}
