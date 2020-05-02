package se.ju.student.hihe1788.laderappen2

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * This fragment controls the connected mower via
 * its joysticks.
 */
class DriveFragment: Fragment() {

    private lateinit var mJoystickThrust: JoystickView
    private lateinit var mJoystickTurn: JoystickView
    private lateinit var mBtnLight: ImageButton
    private lateinit var mBtnHonk: ImageButton
    private lateinit var mBtnBack: ImageButton
    private lateinit var mBtnBluetooth: ImageButton
    private lateinit var mBtnAuto: Button

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

        mBtnLight = requireView().findViewById(R.id.btn_drive_light)
        mBtnHonk = requireView().findViewById(R.id.btn_drive_honk)
        mBtnBack = requireView().findViewById(R.id.btn_drive_back)
        mBtnBluetooth = requireView().findViewById(R.id.btn_drive_bluetooth)
        mBtnAuto = requireView().findViewById(R.id.btn_drive_auto)

        mBtnLight.setOnClickListener {
            if (!mBtnLight.isActivated) {
                mBtnLight.isActivated
                DriveInstructionsModel.setLightOn()
            }
            mBtnLight.isEnabled
            DriveInstructionsModel.setLightOff()
        }

        mBtnHonk.setOnClickListener {
            DriveInstructionsModel.setHonkOn()
        }

        mBtnBack.setOnClickListener {
            println("IT WORX")
        }

        mBtnBluetooth.setOnClickListener {
            println("IT WORX")
        }

        mBtnAuto.setOnClickListener {

            if (!mBtnAuto.isActivated && !MowerModel.isAutonomous)
            {
                mBtnAuto.isActivated
                DriveInstructionsModel.setAutoOn()
            } else if (mBtnAuto.isActivated && MowerModel.isAutonomous)
            {
                mBtnAuto.isEnabled
                DriveInstructionsModel.setAutoOff()
            }
        }

        BluetoothHandler.startSendingDriveInstructions()
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