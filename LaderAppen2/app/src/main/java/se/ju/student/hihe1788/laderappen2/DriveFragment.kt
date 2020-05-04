package se.ju.student.hihe1788.laderappen2

import android.os.Build
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * This fragment controls the connected mower via
 * its joysticks.
 */
class DriveFragment: Fragment() {

    private lateinit var mJoystickThrust: JoystickView
    private lateinit var mJoystickTurn: JoystickView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mJoystickThrust = requireView().findViewById(R.id.joystick_left)
        mJoystickTurn = requireView().findViewById(R.id.joystick_right)
        mJoystickThrust.setToThrust(true)

        // Backbutton
        view.findViewById<ImageButton>(R.id.btn_drive_back)?.setOnClickListener {
            // TODO: Stop autonomous mode if running?
            findNavController().popBackStack()
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