package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RoutesFragment: Fragment() {
    private var theView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (theView == null) {
            //val view = inflater.inflate(R.layout.fragment_routes, container, false)

            //theView = view
        }

        return theView
    }
}