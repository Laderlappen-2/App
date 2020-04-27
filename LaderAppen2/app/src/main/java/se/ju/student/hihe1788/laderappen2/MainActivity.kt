package se.ju.student.hihe1788.laderappen2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.*

public class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mAppContext: Context
    }
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    /**
     * Receives an intent from BluetoothHandler.mHandler and redirect it to the
     * proper place.
     */
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

                    /*val data = intent.getByteArrayExtra("message")
                    val type = data!!.toString()
                        when(type[1]) {
                            Constants.LIGHT_ACK -> {
                                if(type[3] == '1') {
                                    requireViewById<ImageButton>(R.id.btn_drive_light).isSelected
                                }

                            }
                    }*/
                    // save to activeRoute( when a route is finished driveFragment sends to backend)
                }
                Constants.ACTION_ALERT -> {
                    AlertDialog.createSimpleDialog(mAppContext, intent.getStringExtra("message"),
                        "${intent.getStringExtra("message")}. ${MainActivity.mAppContext.getString(R.string.tryAgain)}")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        mAppContext = applicationContext

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController: NavController = host.navController
        mAppBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, mAppBarConfiguration)
        setupBottomNavMenu(navController)

        setupBroadcastReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
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
        val bottomNav: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
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

    // region INITIALIZE BROADCAST RECEIVER(S)
    /**
     * Setup our BroadcastReceiver and registers all filter it should listen on.
     */
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
