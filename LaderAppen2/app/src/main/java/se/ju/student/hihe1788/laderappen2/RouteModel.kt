package se.ju.student.hihe1788.laderappen2

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * A model for a route.
 */
class RouteModel(val id: Int, val createdAt: Date, val events: ArrayList<EventModel>) :Serializable

/**
 * A model for an event
 */
class EventModel(val id: Int, val eventTypeId: Int, val dateCreated: Date, val positionEvent: PointModel, val collisionAvoidanceEvent: PointModel)

/**
 * A model for a point.
 */
class PointModel(val eventId: Int? = null, val eventTypeId: Int, val positionX: Float, val positionY: Float)