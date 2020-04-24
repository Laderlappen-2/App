package se.ju.student.hihe1788.laderappen2

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DriveFragment: Fragment() {

    private lateinit var joystickLeft: JoystickView
    private lateinit var joystickRight: JoystickView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        activity!!.window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                // TODO: The system bars are visible. Make any desired
                // adjustments to your UI, such as showing the action bar or
                // other navigational controls.
            } else {
                // TODO: The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
            }
        }
        */

        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //DO STUFF THAT WE WANT TO DO HERE :D
        joystickLeft = getView()!!.findViewById(R.id.joystick_turn)
        joystickRight = getView()!!.findViewById(R.id.joystick_thrust)
        joystickRight.setToThrust(true)


    }
}