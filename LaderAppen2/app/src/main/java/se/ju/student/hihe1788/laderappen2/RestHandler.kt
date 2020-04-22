package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon

object RestHandler {
    val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1/"
    val DRIVE_SESSION = "drivingsessions"
    val ALL_DRIVE_SESSIONS = "drivingsessions"
    //val PAGINATION_DRIVE_SESSIONS = "drivingsessions?from=1&limit=10"
    val DRIVE_SESSION_BY_ID = "drivingsessions/:sessionId"
    val EVENT = "events"

    fun getAllRoutes() {
        // get all routes
        val url = REST_URL+ ALL_DRIVE_SESSIONS

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            // on success
            Response.Listener {
                //do stuff with response
            },

            Response.ErrorListener {

            })

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postRoute() {
        // first post session
        // receive id for that session
        // loop through points in Route
            // -> for each point: post event
    }

    fun deleteAllRoutes() {

    }
}