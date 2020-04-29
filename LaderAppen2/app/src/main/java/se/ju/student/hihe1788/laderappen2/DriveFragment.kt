package se.ju.student.hihe1788.laderappen2

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

class DriveFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drive_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //DO STUFF THAT WE WANT TO DO HERE :D
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        //or here :D

        /*
        RestHandler.postDriveSession({
            val id = DataHandler.getCurrentRoute().id
            println(id)
        }, {
            println(it?.message)
        })*/

        /*
        val point = PointModel(eventTypeId = 5, positionX =  0, positionY =  0, dateCreated =  Date())

        val positions = ArrayList<PointModel>()
        positions.add(point)

        val route = RouteModel(1, positions)
        RestHandler.postRoute(route, route.id.toString(), {
            println("SUCCESS")
        }, {
            println(it?.message)
        })*/

        RestHandler.test({
            println("SUCCESS")
        }, {
            println(it?.message)
        })
    }
}