package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.*

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mContext: Context
        var mBLEService: BLEService? = null
        var mActivity: MainActivity? = null
    }

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private var mIsBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        mContext = applicationContext
        mActivity = this

        if (!BLEHandler.isSupportingBLE()) {
            println("MOBILE DON'T SUPPORT BLE")
            /** Inform the user that the device isn't supporting BLE */
        }

        if (!BLEHandler.isBluetoothEnabled()) {
            BLEHandler.requestBluetooth()
        }

        setupNavigationComponents()
    }

    override fun onStart() {
        super.onStart()
        setupBroadcastReceiver()
    }

    /** onStop */
    /** User navigates to the Activity */
    /** onStart is called after onRestart */
    override fun onRestart() {
        super.onRestart()
    }

    /** onPause */
    /** User returns to the Activity */
    override fun onResume() {
        super.onResume()
    }

    /** Another Activity comes into the foreground */
    override fun onPause() {
        super.onPause()
    }

    /** The Activity is no longer visible */
    override fun onStop() {
        super.onStop()
        unregisterReceiver(gattUpdateReceiver)
    }

    /** The Activity is finishing or being destroyed by the system */
    override fun onDestroy() {
        super.onDestroy()

        if (mIsBound)
            stopBLEService()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check which request is responded to
        if (requestCode == BLUETOOTH_REQUEST_ENABLE)
        {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                // Thank you for turning on Bluetooth
            } else if (resultCode == RESULT_CANCELED)
            {
                if (mIsBound)
                    stopBLEService()
                // Please turn on Bluetooth
            }
        }
    }

    // region SET UP VIEWS AND NAVIGATION

    private fun setupNavigationComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController: NavController = host.navController
        mAppBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, mAppBarConfiguration)

        setupBottomNavMenu(navController)
    }


    /**
     * Setup the bottom navigation menu
     * @param navController A navigation controller
     */
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)
    }

    /**
     * Navigates to the chosen view from the bottom navigation
     * @param item The item you clicked
     * @return True or false if you could navigate there.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(mAppBarConfiguration)
    }

    //endregion

    private fun setupBroadcastReceiver() {
        val filter = IntentFilter()

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(ACTION_GATT_CONNECTED)
        filter.addAction(ACTION_GATT_DISCONNECTED)
        filter.addAction(ACTION_GATT_SERVICES_DISCOVERED)
        filter.addAction(ACTION_DATA_WRITTEN)
        filter.addAction(ACTION_GATT_REGISTER_CHARACTERISTIC_READ)

        registerReceiver(gattUpdateReceiver, filter)
    }

    fun startBLEService() {
        //Initiate Service, start it and then bind to it.
        val serviceClass = BLEService::class.java
        val intent = Intent(applicationContext, serviceClass)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )
    }

    fun stopBLEService() {
        //Initiate Service, start it and then bind to it.
        val serviceClass = BLEService::class.java
        val intent = Intent(applicationContext, serviceClass)
        stopService(intent)
    }

    //Returns an object used to access public methods of the bluetooth service
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as BLEService.MyLocalBinder
            mBLEService = binder.getService()
            mIsBound = true
            Log.i(TAG, "Bind connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "Bind disconnected")
            mIsBound = false
        }
    }

    private val gattUpdateReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            println("BTStateReceiver: onReceive()")
            val action = intent?.action
            val data = intent?.data

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
            else if ( action.equals(ACTION_GATT_DISCONNECTED) )
            {
                println("BTStateReceiver: onReceive(): ACTION_GATT_DISCONNECTED")
            }
            else if ( action.equals(ACTION_GATT_SERVICES_DISCOVERED) )
            {
                println("BTStateReceiver: onReceive(): ACTION_GATT_SERVICES_DISCOVERED")
            }
            else if ( action.equals(ACTION_DATA_WRITTEN) )
            {
                println("BTStateReceiver: onReceive(): ACTION_DATA_WRITTEN")

                if (data != null)
                {
                    // PROCESS DATA
                }
            }
            else if ( action.equals(ACTION_GATT_REGISTER_CHARACTERISTIC_READ) )
            {
                Log.i(TAG, "ACTION_GATT_REGISTER_CHARACTERISTIC_READ")
                mBLEService!!.readChar()
            }
        }
    }
}
