package se.ju.student.hihe1788.laderappen2

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class RouteModel(val id: Int, val createdAt: Date, val events: ArrayList<EventModel>) :Serializable

/*eventTypeId: 3 = collisionAvoidance, 5 = position */
class EventModel(val id: Int, val eventTypeId: Int, val dateCreated: Date, val positionEvent: PointModel, val collisionAvoidanceEvent: PointModel)

class PointModel(val eventId: Int? = null, val eventTypeId: Int, val positionX: Float, val positionY: Float)
