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

class PointModel(val eventId: Int, val positionX: Int, val positionY: Int, val dateCreated: Date) {}
