package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class HomeFragment: Fragment() {
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

        view.findViewById<Button>(R.id.bntDrive)?.setOnClickListener {
            if (!BluetoothHandler.isBluetoothEnabled()) {
                //check if user wants to enable bluetooth
                context?.let { it1 -> AlertDialog.acceptBtDialog(it1, "Bluetooth", "You need bluetooth for this.\nDo you want to activate bluetooth?") }
            }
            //navigate to DriveFragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}