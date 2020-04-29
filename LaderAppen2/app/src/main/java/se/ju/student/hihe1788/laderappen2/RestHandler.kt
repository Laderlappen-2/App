package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon

object RestHandler {
    val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1/"
    val ALL_DRIVE_SESSIONS = "drivingsessions"      //?from=1&limit=10"
    val DRIVE_SESSION_BY_ID = "drivingsessions/:"   // + sessionId

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

        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postRoute() {
        // waiting for endpoint
        val url = ""

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
            Response.Listener { response ->
                println("Response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                println("ERROR: ${error.toString()}")
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
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

        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
    }
}