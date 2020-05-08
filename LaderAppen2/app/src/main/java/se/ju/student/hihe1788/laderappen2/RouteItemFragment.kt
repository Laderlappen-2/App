package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

class RouteItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.route_item_fragement, container, false)

        val pList = arrayListOf(
            PointModel(-1, 5, 30, -40, Date()),
            PointModel(-1, 5, -5, -10, Date()),
            PointModel(-1, 5, 17, -8, Date()),
            PointModel(-1, 5, 9, 10, Date())
        )

        val cList = arrayListOf(
            PointModel(-1, 3, -40, 70, Date()),
            PointModel(-1, 3, 7, -7, Date()),
            PointModel(-1, 3, -25, 5, Date()),
            PointModel(-1, 3, 30, 20, Date())
        )

        val route = RouteModel(-1337, pList, cList)


        view.findViewById<RouteCanvasView>(R.id.canvas_view)?.updatePoints(route)

        return view
    }
}