package se.ju.student.hihe1788.laderappen2

import java.util.*
import kotlin.collections.ArrayList

class RouteModel(val id: Int, val positionEvents: ArrayList<PointModel> = ArrayList(), val collisionAvoidanceEvents: ArrayList<PointModel> = ArrayList()) {

    fun getPositions(): ArrayList<PointModel> {
        return positionEvents
    }

    fun getCollisonAvoidances(): ArrayList<PointModel> {
        return collisionAvoidanceEvents
    }
}


class PointModel(val eventId: Int? = null, val eventTypeId: Int, val positionX: Float, val positionY: Float, val dateCreated: Date) {
    /*3 collison 5 posision*/
}
