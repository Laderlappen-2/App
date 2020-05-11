package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import java.util.*
import kotlin.collections.ArrayList

class RouteItemFragment : Fragment() {
    private lateinit var mRoute: RouteModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.route_item_fragement, container, false)

        val safeArgs: RouteItemFragmentArgs by navArgs()
        mRoute = safeArgs.routeArgs

        view.findViewById<RouteCanvasView>(R.id.canvas_view)?.updatePoints(mRoute)
        return view
    }
}