package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import se.ju.student.hihe1788.laderappen2.util.RestHandler
import java.util.*

/**
 * This fragment controls the connected mower via
 * its joysticks.
 */
class DriveFragment: Fragment() {

    private lateinit var mJoystickThrust: JoystickView
    private lateinit var mJoystickTurn: JoystickView
    private lateinit var attachedContext: Context
    private var hasActiveDrivingSession: Boolean = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.attachedContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mJoystickThrust = requireView().findViewById(R.id.joystick_left)
        mJoystickTurn = requireView().findViewById(R.id.joystick_right)
        mJoystickThrust.setToThrust(true)

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

    private fun backButtonAction() {
        // TODO: Stop autonomous mode if running?
        if(hasActiveDrivingSession)
            saveCurrentDrivingSession()
        else
            findNavController().popBackStack()
    }

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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}