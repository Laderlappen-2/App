package se.ju.student.hihe1788.laderappen2

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * A model for a route, aka. driving session.
 * @property id The id of the route model
 * @property createdAt The date the route was created
 * @property events An array list containing all events for this route
 */
class RouteModel(val id: Int, val createdAt: Date, val events: ArrayList<EventModel>) :Serializable

/**
 * A model for an event
 * @property id The id of the event
 * @property eventTypeId The event type of the event
 * @property dateCreated The date the event was created
 * @property positionEvent If the event is a position event, this property contains it's data
 * @property collisionAvoidanceEvent If the event is a collision avoidance event, this property contains it's data
 */
class EventModel(val id: Int? = null, val eventTypeId: Int, val dateCreated: Date, val positionEvent: PointModel? = null, val collisionAvoidanceEvent: PointModel? = null)

/**
 * A model for a point.
 * @property eventId The id of the event that this point model is related to
 * @property eventTypeId The event type id
 * @property positionX Position X for the point
 * @property positionY Position Y for the point
 */
class PointModel(val eventId: Int? = null, val eventTypeId: Int, val positionX: Float, val positionY: Float)