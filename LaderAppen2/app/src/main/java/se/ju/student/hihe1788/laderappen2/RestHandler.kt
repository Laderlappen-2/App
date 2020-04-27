package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONObject

object RestHandler {
    private const val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1"
    private const val ALL_DRIVE_SESSIONS = "/drivingsessions"      //?from=1&limit=10"
    private const val DRIVE_SESSION_BY_ID = "/drivingsessions"   // + sessionId
    private const val EVENTS = "/events"

    fun getAllRoutes(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$ALL_DRIVE_SESSIONS"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                parseResponse<PaginationModel>(it, { paginationModel ->
                    paginationModel?.let { DataHandler.setPagination(paginationModel) }
                    successCallback()
                }, errorCallback)
            },
            Response.ErrorListener { error ->
                errorCallback(getRestError(error))
            })
        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postDriveSession(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$ALL_DRIVE_SESSIONS"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
            Response.Listener() { response ->
                parseResponse<Unit>(response, {
                    val routeId = response.getInt("id")
                    DataHandler.setCurrentRoute(RouteModel(routeId))
                    successCallback()
                }, errorCallback)
            },

            Response.ErrorListener { error ->
                errorCallback(getRestError(error))
            }
        )
        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun postRoute(routes: RouteModel, id: String, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$DRIVE_SESSION_BY_ID/$id$EVENTS"
        val jsonString = Klaxon().toJsonString(routes)
        val jsonObj = JSONObject(jsonString)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObj,
            Response.Listener { response ->
                parseResponse<Unit>(response, {
                    successCallback()
                }, errorCallback)
            },
            Response.ErrorListener { error ->
                errorCallback(getRestError(error))
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    fun deleteRouteById(id: Int, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$DRIVE_SESSION_BY_ID/$id"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.DELETE, url, null,
            Response.Listener { response ->
                parseResponse<Unit>(response, {
                    successCallback()
                }, errorCallback)
            },
            Response.ErrorListener { error ->
                errorCallback(getRestError(error))
            }
        )

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonObjectRequest)
    }

    inline fun<reified T> parseResponse(json: JSONObject, done: (T?) -> Unit, error: (RestErrorModel?) -> Unit) {
        if(!json.has("error")) {
            var responseObj: T? = null
            if(Unit !is T) {
                responseObj = Klaxon().parse<T>(json.toString())
            }
            done(responseObj)
        }else{
            val restError = Klaxon().parse<RestErrorModel>(json.toString())
            error(restError)
        }
    }

    private fun getRestError(error: VolleyError): RestErrorModel{
        return RestErrorModel(0, error.message.orEmpty(), error.localizedMessage.orEmpty())
    }
}