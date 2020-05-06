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
            PointModel(-1, 5, 4, 4, Date()),
            PointModel(-1, 5, -5, 6, Date()),
            PointModel(-1, 5, 7, -8, Date()),
            PointModel(-1, 5, 9, 1, Date())
        )

        val cList = arrayListOf(
            PointModel(-1, 3, 4, 1, Date()),
            PointModel(-1, 3, 7, -7, Date()),
            PointModel(-1, 3, -3, 5, Date()),
            PointModel(-1, 3, 9, 1, Date())
        )

        val route = RouteModel(-1337, pList, cList)


        view.findViewById<RouteCanvasView>(R.id.canvas_view)?.updatePoints(route)

        return view
    }
}