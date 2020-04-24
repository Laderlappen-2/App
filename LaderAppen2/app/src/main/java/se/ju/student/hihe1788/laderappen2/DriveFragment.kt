package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class DriveFragment: Fragment() {

    private lateinit var joystickThrust: JoystickView
    private lateinit var joystickTurn: JoystickView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).requestedOrientation.apply {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        /**
         * CHANGE TO LANDSCAPE ORIENTATION
        activity!!.requestedOrientation.apply {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        */

        /**
         * HIDE NAVIGATION BAR
        activity!!.window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        */

        (activity as AppCompatActivity).window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                // TODO: The system bars are visible. Make any desired
                // adjustments to your UI, such as showing the action bar or
                // other navigational controls.
            } else {
                // TODO: The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
            }
        }

        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        joystickThrust = getView()!!.findViewById(R.id.joystick_left)
        joystickTurn = getView()!!.findViewById(R.id.joystick_right)
        joystickThrust.setToThrust(true)


    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}