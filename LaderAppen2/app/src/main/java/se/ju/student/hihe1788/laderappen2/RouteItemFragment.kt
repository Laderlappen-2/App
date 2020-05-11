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
            PointModel(-1, 5, 30f, -40f, Date()),
            PointModel(-1, 5, -5f, -10f, Date()),
            PointModel(-1, 5, 17f, -8f, Date()),
            PointModel(-1, 5, 9f, 10f, Date())
        )

        val cList = arrayListOf(
            PointModel(-1, 3, -40f, 70f, Date()),
            PointModel(-1, 3, 7f, -7f, Date()),
            PointModel(-1, 3, -25f, 5f, Date()),
            PointModel(-1, 3, 30f, 20f, Date())
        )

        val route = RouteModel(-1337, pList, cList)


        view.findViewById<RouteCanvasView>(R.id.canvas_view)?.updatePoints(route)

        return view
    }
}