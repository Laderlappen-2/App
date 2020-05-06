package se.ju.student.hihe1788.laderappen2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.*

val REQUEST_ENABLE_BT = 1

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mContext: Context
        lateinit var mBLEHandler: BLEHandler
    }

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mBTStateReceiver: BTStateReceiver
    private lateinit var mBLEDeviceScanner: BLEDeviceScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        mContext = applicationContext
        mBTStateReceiver = BTStateReceiver(mContext)
        mBLEHandler = BLEHandler
        mBLEDeviceScanner = BLEDeviceScanner()

        if (!mBLEHandler.isSupportingBLE())
        {
            println("MOBILE DON'T SUPPORT BLE")
            /** Inform the user that the device isn't supporting BLE */
        }

        if (!mBLEHandler.isBluetoothEnabled())
        {
            mBLEHandler.requestBluetooth()
        }

        setupNavigationComponents()
    }

    override fun onStart() {
        super.onStart()
        setupBroadcastReceiver()
        //mBLEDeviceScanner.start()
        //mBLEHandler.connectTo(BLEDevice)
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
        unregisterReceiver(mBTStateReceiver)
    }

    /** The Activity is finishing or being destroyed by the system */
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check which request is responded to
        if (requestCode == REQUEST_ENABLE_BT)
        {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                // Thank you for turning on Bluetooth
            } else if (resultCode == RESULT_CANCELED)
            {
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
        filter.addAction(ACTION_GATT_SERVICES_DISCOVERED)

        registerReceiver(mBTStateReceiver, filter)
    }

}
