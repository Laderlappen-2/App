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

    companion object {
        lateinit var appContext: Context
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            when (action) {
                Constants.ACTION_STATE_CONNECTING -> {
                    //loading screen??
                }
                Constants.ACTION_STATE_CONNECTED ->
                    MowerModel.isConnected = true

                Constants.ACTION_STATE_LISTEN ->
                    MowerModel.isConnected = false

                Constants.ACTION_STATE_NONE ->
                    MowerModel.isConnected = false

                Constants.ACTION_MSG_RECEIVED -> {
                    // save to activeRoute( when a route is finished driveFragment sends to backend)
                }
                Constants.ACTION_ALERT -> {
                    AlertDialog.createSimpleDialog(appContext, intent.getStringExtra("message"),
                        "${intent.getStringExtra("message")}. Try again")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        appContext = applicationContext

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController: NavController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        setupBroadcastReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // region SET UP VIEWS AND NAVIGATION

    private fun setupActionBar(navController: NavController, appBarconfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarconfig)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    //endregion

    // region INITIALIZE BROADCAST RECEIVER(S)
    private fun setupBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(Constants.ACTION_STATE_CONNECTING)
        filter.addAction(Constants.ACTION_STATE_CONNECTED)
        filter.addAction(Constants.ACTION_STATE_LISTEN)
        filter.addAction(Constants.ACTION_STATE_NONE)
        filter.addAction(Constants.ACTION_MSG_RECEIVED)
        filter.addAction(Constants.ACTION_ALERT)

        registerReceiver(mBroadcastReceiver, filter)
    }

    // endregion

}
