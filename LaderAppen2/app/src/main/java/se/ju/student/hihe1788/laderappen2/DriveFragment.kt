package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import se.ju.student.hihe1788.laderappen2.MainActivity.MainActivity.Companion.mActivity
import java.io.IOException

private val TAG = DriveFragment::class.java.simpleName
import androidx.navigation.fragment.findNavController
import se.ju.student.hihe1788.laderappen2.util.RestHandler
import java.util.*

/**
 * This fragment controls the connected mower via
 * its joysticks.
 */
class DriveFragment: Fragment() {

    private lateinit var mJoystickLeft: JoystickView
    private lateinit var mJoystickRight: JoystickView
    private lateinit var mBtnLight: ImageButton
    private lateinit var mBtnHonk: ImageButton
    private lateinit var mBtnBack: ImageButton
    private lateinit var mBtnBluetooth: ImageButton
    private lateinit var mBtnAuto: Button
    private lateinit var attachedContext: Context
    private var hasActiveDrivingSession: Boolean = false

    /**
     * Override function that returns the view for DriveFragment.
     * @see R.layout.drive_fragment
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    /**
     * Override function that sets the context for DriveFragment.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.attachedContext = context
    }

    /**
     * Override function that sets up the view for DriveFragment, Initiates joysticks and sets onClickListeners.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mJoystickLeft = requireView().findViewById(R.id.joystick_left)
        mJoystickRight = requireView().findViewById(R.id.joystick_right)
        mJoystickLeft.setToThrust(true)

        mBtnLight = requireView().findViewById(R.id.btn_drive_light)
        mBtnHonk = requireView().findViewById(R.id.btn_drive_honk)
        mBtnBack = requireView().findViewById(R.id.btn_drive_back)
        mBtnBluetooth = requireView().findViewById(R.id.btn_drive_bluetooth)
        mBtnAuto = requireView().findViewById(R.id.btn_drive_auto)

        mBtnLight.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_SEND_LIGHTS
            if (!mBtnLight.isActivated) {
                mBtnLight.isActivated
                DriveInstructionsModel.setLightOn()
            } else {
                mBtnLight.isEnabled
                DriveInstructionsModel.setLightOff()
            }

            MainActivity.mContext.sendBroadcast(intent)
        }

        mBtnHonk.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_SEND_HONK
            DriveInstructionsModel.setHonkOn()
            MainActivity.mContext.sendBroadcast(intent)
        }

        mBtnBack.setOnClickListener {

        }

        mBtnBluetooth.setOnClickListener {
            try {
                MainActivity.mActivity?.startBLEService()
                MowerModel.isConnected = true
            } catch (e: IOException) {
                Log.i(TAG, "Could not connect to Mower. Msg: $e")
                MowerModel.isConnected = false
            }

        }

        mBtnAuto.setOnClickListener {
            val intent = Intent()


            if (!mBtnAuto.isActivated)
            {
                mBtnAuto.isActivated
                DriveInstructionsModel.setAutoOn()
                intent.action = ACTION_SEND_AUTO
            } else if (mBtnAuto.isActivated)
            {
                mBtnAuto.isEnabled
                DriveInstructionsModel.setAutoOff()
                intent.action = ACTION_SEND_MANUAL
            }
            MainActivity.mContext.sendBroadcast(intent)
        }

        // On GUI back button click
        view.findViewById<ImageButton>(R.id.btn_drive_back)?.setOnClickListener {
            backButtonAction()
        }
        // On "hardware" back button click
        /* TODO Make this work :)
        view.setOnKeyListener(View.OnKeyListener() { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                backButtonAction()
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })*/

        // If connected, create a driving session at REST API
        if(MowerModel.isConnected) {
            RestHandler.createDrivingSession({
                // Request done, hide loading
                hasActiveDrivingSession = true
                Toast.makeText(this.attachedContext, "Driving session id: " + DataHandler.getCurrentRoute().id, Toast.LENGTH_SHORT).show()
            }, { error ->
                // Request done, hide loading
                Toast.makeText(this.attachedContext, "Error: " + error?.error, Toast.LENGTH_LONG).show()
            })
        }else{
            hasActiveDrivingSession = false
        }
    }

    /**
     *  Calls saveCurrentDrivingSession if a drive session is active, else navigates back to HomeFragment.
     *  @see saveCurrentDrivingSession
     *  @see HomeFragment
     */
    private fun backButtonAction() {
        // TODO: Stop autonomous mode if running?
        if(hasActiveDrivingSession)
            saveCurrentDrivingSession()
        else
            findNavController().popBackStack()

    }

    /**
     *  Save finished route to database. Then navigates back to HomeFragment.
     *  @see RestHandler.createBatchEvents
     *  @see HomeFragment
     */
    private fun saveCurrentDrivingSession() {
        if(hasActiveDrivingSession) {
            RestHandler.createBatchEvents(DataHandler.getCurrentRoute(), {
                // Success
                Toast.makeText(this.attachedContext, "Saved route", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }, { error ->
                Toast.makeText(this.attachedContext, "Error: " + error?.error, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            })
        }
    }

    /**
     * Override function that hides action bar on resume.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setupBroadcastReceiverFilter()
    }

    /**
     * Override function that shows action bar on pause.
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(mBroadcastReceiver)
    }

    private fun setupBroadcastReceiverFilter() {
        val filter = IntentFilter()
        filter.addAction(ACTION_DATA_RECEIVED_FROM_MOWER)
        activity?.registerReceiver(mBroadcastReceiver, filter)
    }


    private val mBroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_DATA_RECEIVED_FROM_MOWER -> {
                    val data = intent.getByteArrayExtra("data")
                    Log.i(TAG, "mBroadcastReceiver - Data received from mower")
                    Log.i(TAG, "DATA = $data")
                    /* decodeIncomingMsg(data) */
                }
            }
        }

    }

    /**
     * TODO - Sort out the regex split and set it to a appropriate type. (MowerModel perhaps?)
     * Also in need of some sanity checks in case we get some "slaskvärden".
     * Don't forget to send back an ACK via [MainActivity.commandsToMowerReceiver] in case it's needed.
     */
    private fun decodeIncomingMsg(data: ByteArray) {
        try {
            val s = data.toString()
            val pattern = Regex("[0-9]+")
            val matches = pattern.findAll(s)
            println("decodeIncomingMsg, this should be a 1/0 = "+matches.elementAt(1))
        } catch (e: IOException) {
            Log.i(TAG, "Could not decode incoming message. Msg: $e")
        }
    }


}