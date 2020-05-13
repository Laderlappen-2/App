package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
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

/**
 * MainActivity
 */
class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mContext: Context
        var mActivity: MainActivity? = null
    }

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private var mIsBound = false
    private lateinit var mBLEService: BLEService

    /**
     * Override function that sets up host fragment, app navigation and calls setupActionBar(), setupBottomNavMenu()
     * and setupBroadcastReceiver().
     * @see R.id.my_nav_host_fragment
     * @see setupActionBar
     * @see setupBottomNavMenu
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
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
            BLEHandler.toggleBluetooth()
        }

        setupNavigationComponents()
    }

    /**
     * A lifecycle function that runs after [onResume]
     * and setup broadcastReceivers
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onStart() {
        super.onStart()
        setupBroadcastReceiver()
    }

    /**
     * A lifecycle function that runs after [onPause]
     * and unregisters all broadcastReceivers
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onStop() {
        super.onStop()
        unregisterReceiver(gattUpdateReceiver)
        unregisterReceiver(commandsToMowerReceiver)
    }

    /**
     *  Override function that unregisters broadcast receivers on destroy.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onDestroy() {
        super.onDestroy()

        if (mIsBound)
            stopBLEService()
    }

    // region SET UP VIEWS AND NAVIGATION
    /**
     * Handles all navigation in app.
     */
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

    // region SET UP VIEWS AND NAVIGATION

    /**
     * Setup the action bar.
     * @param navController A navigation controller
     * @param appBarconfig The configuration for the appBar
     */
    private fun setupActionBar(navController: NavController, appBarconfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarconfig)
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
     * Override function that navigates to the chosen view from the bottom navigation
     * @param item The item you clicked
     * @return True or false if you could navigate there.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    /**
     * Override function that handles navigation.
     * @return True or false if you could navigate there.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(mAppBarConfiguration)
    }

    //endregion
    /**
     * Setups broadcastReceivers and adds actions.
     */
    private fun setupBroadcastReceiver() {
        val filter1 = IntentFilter()

        filter1.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter1.addAction(ACTION_GATT_CONNECTED)
        filter1.addAction(ACTION_GATT_DISCONNECTED)
        filter1.addAction(ACTION_GATT_SERVICES_DISCOVERED)
        filter1.addAction(ACTION_DATA_WRITTEN)
        filter1.addAction(ACTION_GATT_REGISTER_CHARACTERISTIC_READ)

        registerReceiver(gattUpdateReceiver, filter1)

        val filter2 = IntentFilter()

        filter2.addAction(ACTION_SEND_HONK)
        filter2.addAction(ACTION_SEND_LIGHTS)
        filter2.addAction(ACTION_SEND_QUIT)
        filter2.addAction(ACTION_SEND_AUTO)
        filter2.addAction(ACTION_SEND_MANUAL)
        filter2.addAction(ACTION_CONNECT_TO_MOWER)
        filter2.addAction(ACTION_SEND_ACK_TO_MOWER)

        registerReceiver(commandsToMowerReceiver, filter2)

    }

    /**
     * Starts the Bluetooth Low Energy-service and binds it
     * to this activity.
     */
    fun startBLEService() {
        //Initiate Service, start it and then bind to it.
        val serviceClass = BLEService::class.java
        val intent = Intent(applicationContext, serviceClass)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )
    }

    /**
     * Stops the Bluetooth Low Energy-service and unbinds it
     * from this activity.
     */
    fun stopBLEService() {
        //Initiate Service, start it and then bind to it.
        val serviceClass = BLEService::class.java
        val intent = Intent(applicationContext, serviceClass)
        stopService(intent)
        unbindService(myConnection)
    }

    //Returns an object used to access public methods of the bluetooth service
    private val myConnection = object : ServiceConnection {
        /**
         * Override function that runs when the service has connected
         * and binds the activity to the service.
         */
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BLEService.MyLocalBinder
            mBLEService = binder.getService()
            mIsBound = true
            Log.i(TAG, "Bind connected")
        }

        /**
         * Override function that runs when the service has disconnected.
         */
        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "Bind disconnected")
            mIsBound = false
        }
    }


    private val commandsToMowerReceiver = object : BroadcastReceiver() {
        /**
         * Receives actions from [DriveFragment] and sends them to the [BLEService]
         * @param context: [MainActivity]'s context.
         * @param intent: A intent with destination and data.
         */
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_SEND_LIGHTS -> {
                    mBLEService.send(DriveInstructionsModel.getLightAsByteArray())
                }
                ACTION_SEND_HONK -> {
                    mBLEService.send(DriveInstructionsModel.getHonkAsByteArray())
                }
                ACTION_SEND_MANUAL,
                ACTION_SEND_AUTO -> {
                    mBLEService.send(DriveInstructionsModel.getDriveModeAsByteArray())
                }
                ACTION_SEND_QUIT -> {
                    mBLEService.send(DriveInstructionsModel.getTurnOffCmdAsByteArray())
                }
                ACTION_CONNECT_TO_MOWER -> {
                    startBLEService()
                }
                ACTION_SEND_ACK_TO_MOWER -> {
                    mBLEService.send(DriveInstructionsModel.getAckAsByteArray())
                }
            }
        }

    }

    private val gattUpdateReceiver = object : BroadcastReceiver() {
        /**
         * Receives actions from [BLEService] containing information
         * about the BLE-status.
         * @param context: [MainActivity]'s context.
         * @param intent: A intent with destination and data.
         */
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

            }
        }
    }
}
