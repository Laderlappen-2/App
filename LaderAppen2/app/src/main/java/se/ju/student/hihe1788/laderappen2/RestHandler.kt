package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon
import org.json.JSONObject

object RestHandler {
    val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1"
    val ALL_DRIVE_SESSIONS = "/drivingsessions"      //?from=1&limit=10"
    val DRIVE_SESSION_BY_ID = "/drivingsessions"   // + sessionId
    val EVENTS = "/events/batch"

    fun getAllRoutes() {
        val url = REST_URL+ ALL_DRIVE_SESSIONS

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val newPagination = Klaxon().parse<PaginationModel>(it.toString())

                newPagination?.let { DataHandler.setPagination(newPagination) }
            },

            Response.ErrorListener {error ->
                println("ERROR: ${error.toString()}")
            })

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postDriveSession() {
        val url = REST_URL + ALL_DRIVE_SESSIONS

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
            Response.Listener {
                //responsebody
                /**
                 {
                "collisions": [], // collisionAvoidanceEvent?
                "paths": [], //positionEvent ?
                "id": 123
                }
                 */
            },

            Response.ErrorListener { error ->
                println("ERROR: ${error.toString()}")
            }
        )
    }

    fun postRoute(routes: RouteModel, id: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        val url = REST_URL + DRIVE_SESSION_BY_ID + "/$id" + EVENTS
        val jsonString = Klaxon().toJsonString(routes)
        val jsonObj = JSONObject(jsonString)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObj,
            Response.Listener { response ->
                println("Response: %s".format(response.toString()))
                /*
                if(response.statusCode == 201)
                  successCallback()
                else
                  errorCallback(parsaTillErrorObject)*/
            },
            Response.ErrorListener { error ->
                println("ERROR: ${error.toString()}")
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun deleteRouteById(id: Int) {
        val url = DRIVE_SESSION_BY_ID + id.toString()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.DELETE, url, null,
            Response.Listener { response ->
                println("Response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                println("ERROR: ${error.toString()}")
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }
}