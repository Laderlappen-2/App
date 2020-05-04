package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Start view for the LäderAppen
 */
class HomeFragment: Fragment() {

    private lateinit var mBLEHandler: BLEHandler

    /** Fragment has been attached to an activity.
     * context is hosting this fragment
     **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    /** Time to draw this fragments' UI for the first time.
     * To draw a UI for the fragment, a View component must be returned
     * from this method which is the root of the fragment’s layout.
     * We can return null if the fragment does not provide a UI
     **/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBLEHandler = BLEHandler(MainActivity.mContext)

        view.findViewById<Button>(R.id.bntDrive)?.setOnClickListener {
            // TODO ask if user wants to enable bluetooth with AlertDialog if yes navigate to drive
            //findNavController().navigate(R.id.driveFragment, null)
            MainActivity.mBLEHandler.connectTo(BLEDevice)
        }
    }

    /** This will be called after onCreate() and onCreateView(),
     * to indicate that the activity’s onCreate() has completed.
     * If there is something that’s needed to be initialised in the fragment
     * that depends upon the activity’s onCreate() having completed its work
     * then onActivityCreated() can be used for that initialisation work
     **/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * Called when the fragment gets visible
     **/
    override fun onStart() {
        super.onStart()
    }

    /**
     * The system calls this method as the first indication that the user is leaving the fragment.
     * This is usually where you should commit any changes that should be persisted beyond the current user session
     **/
    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * This is the counterpart to onCreateView() where we set up the UI.
     * If there are things that are needed to be cleaned up specific to the UI,
     * then that logic can be put up in onDestroyView()
     **/
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * Do final clean up of the fragment’s state.
     * Not guaranteed to be called by the Android platform.
     **/
    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * The fragment has been disassociated from the hosting activity
     **/
    override fun onDetach() {
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}