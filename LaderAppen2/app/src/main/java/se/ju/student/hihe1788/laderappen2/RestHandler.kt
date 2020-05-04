package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

object RestHandler {
    private const val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1"
    private const val ALL_DRIVE_SESSIONS = "/drivingsessions"      //?from=1&limit=10"
    private const val DRIVE_SESSION_BY_ID = "/drivingsessions"   // + sessionId
    private const val EVENTS = "/events"
    private val gson = Gson()

    fun getAllRoutes(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$ALL_DRIVE_SESSIONS"

        val jsonObjectRequest = CustomRestRequest(Request.Method.GET, url, null,
            Response.Listener {
                successCallback()
            },
            Response.ErrorListener { error ->
                parseErrorResponse(error, errorCallback)
            })
        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postDriveSession(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$ALL_DRIVE_SESSIONS"

        val jsonObjectRequest = CustomRestRequest(Request.Method.POST, url, null,
            Response.Listener() { response ->
                parseStringResponse<JsonObject>(response) { parsedResponse ->
                    val routeId = parsedResponse?.get("id")?.asInt
                    routeId?.let {
                        DataHandler.setCurrentRoute(RouteModel(routeId))
                        successCallback()
                    }
                }
            },
            Response.ErrorListener { error ->
                parseErrorResponse(error, errorCallback)
            }
        )
        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postRoute(route: RouteModel, id: String, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$DRIVE_SESSION_BY_ID/$id$EVENTS"

        val temPoints = ArrayList<PointModel>()
        temPoints.addAll(route.positionEvents)
        temPoints.addAll(route.collisionAvoidanceEvents)

        val jsonString = Klaxon().toJsonString(temPoints)

        val jsonArrayRequest = CustomRestRequest(Request.Method.POST, url, jsonString,
            Response.Listener { responseString ->
                parseStringResponse<JsonObject>(responseString) { jsonObject ->
                    successCallback()
                }
            },
            Response.ErrorListener {  error ->
                parseErrorResponse(error, errorCallback)
            })

        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonArrayRequest)
    }

    fun deleteRouteById(id: Int, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$DRIVE_SESSION_BY_ID/$id"

        val jsonObjectRequest = CustomRestRequest(Request.Method.DELETE, url, null,
            Response.Listener {
                successCallback()
            },
            Response.ErrorListener { error ->
                parseErrorResponse(error, errorCallback)
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.mAppContext).addToRequestQueue(jsonObjectRequest)
    }

    private inline fun<reified T> parseStringResponse(jsonString: String, done: (T?) -> Unit) {
        val json = gson.fromJson(jsonString, JsonObject::class.java)

        if(json == null) {
            error(RestErrorModel(0, "Invalid json", "JSON string is not valid"))
            return
        }

        val responseObj: T? = gson.fromJson(jsonString, T::class.java)
        done(responseObj)
    }

    private fun parseErrorResponse(error: VolleyError, done: (RestErrorModel) -> Unit) {
        try {
            val charset = HttpHeaderParser.parseCharset(error.networkResponse.headers)
            val jsonString = String(error.networkResponse.data, Charset.forName(charset))
            val restError = gson.fromJson(jsonString, RestErrorModel::class.java)
            restError.statusCode = error.networkResponse.statusCode
            done(restError)
        }catch(ex: Exception) {
            // Any exception would be catched here, and then returned to the calling function
            val restError = RestErrorModel(0, ex.message.orEmpty(), ex.localizedMessage.orEmpty())
            restError.statusCode = error.networkResponse.statusCode
            done(restError)
        }
    }
}