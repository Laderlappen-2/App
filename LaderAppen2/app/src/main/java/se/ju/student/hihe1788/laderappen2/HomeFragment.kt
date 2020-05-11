package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Start view for the LäderAppen
 */
class HomeFragment: Fragment() {

    /**
     * Override function that returns the view for HomeFragment.
     * @see R.layout.fragment_home
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * Override function that sets an on click listener to drive button.
     * @see R.layout.fragment_home
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.bntDrive)?.setOnClickListener {
            // TODO ask if user wants to enable bluetooth with AlertDialog if yes navigate to drive
            findNavController().navigate(R.id.driveFragment, null)
        }
    }

    /**
     * Override function that creates the menu that is displayed in the action bar.
     * @see R.menu.main_menu
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}