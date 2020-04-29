package se.ju.student.hihe1788.laderappen2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

object RestHandler {
    private const val REST_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1"
    private const val ALL_DRIVE_SESSIONS = "/drivingsessions"      //?from=1&limit=10"
    private const val DRIVE_SESSION_BY_ID = "/drivingsessions"   // + sessionId
    private const val EVENTS = "/events"

    fun test(successCallback: (com.google.gson.JsonObject?) -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        var req = CustomRestRequest(Request.Method.POST, REST_URL + "/events", "{\"drivingSessionId\":1,\"eventTypeId\":0,\"eventData\":{\"positionX\":1,\"positionY\":1},\"dateCreated\":\"2020-04-29 11:28:00\"}", Response.Listener<String> { responseString ->
            println("response")
            println(responseString)
            parseStringResponse<com.google.gson.JsonObject>(responseString, { parsedJsonObject ->
                // ett korrekt svar, parsat till <typen man skickade in> (i detta fall com.google.gson.JsonObject)
                successCallback(parsedJsonObject)
            }, { error ->
                // rest api error, parsat till RestErrorModel
                println("vi fick ett error från rest api, dock inte network error, utan typ status 400")
                println("error msg: " + error?.message)
                errorCallback(error)
            })
        }, Response.ErrorListener{ error ->
            // network error typ, eller nåt, vi fick inget svar från rest api iaf
            println("error vid customrequest")
            println(error)
            error.printStackTrace()
            errorCallback(getRestError(error))
        })
        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(req)
    }

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

        val temPoints = ArrayList<PointModel>()
        temPoints.addAll(routes.positionEvents)
        temPoints.addAll(routes.collisionAvoidanceEvents)

        //val pointsJson = JSONArray(routes.mPositionEvents)
        //val collisionPointsJson = JSONArray(routes.mCollisionAvoidanceEvents)
        val jsonString = Klaxon().toJsonString(temPoints)
        //val allPoints = JSONArray(jsonString)

        val jsonArrayRequest = CustomRestRequest(Request.Method.POST, url, jsonString,
            Response.Listener { responseString ->
                println("response")
                println(responseString)
                parseStringResponse<com.google.gson.JsonObject>(responseString, { jsonObject ->
                    println("Vi skapade " + jsonObject?.get("count") + " events för driving session " + id)
                    successCallback()
                }, errorCallback)
            },
            Response.ErrorListener {  error ->
                errorCallback(getRestError(error))
            })
        /*
        val jsonString = Klaxon().toJsonString(allPoints)
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
        )*/

        RequestQueueSingleton.getInstance(MainActivity.appContext).addToRequestQueue(jsonArrayRequest)
    }

    fun deleteRouteById(id: Int, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$REST_URL$DRIVE_SESSION_BY_ID/$id"

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

    inline fun<reified T> parseStringResponse(jsonString: String, done: (T?) -> Unit, error: (RestErrorModel?) -> Unit) {
        val json = Gson().fromJson(jsonString, com.google.gson.JsonObject::class.java)

        if(json == null) {
            error(RestErrorModel(0, "Gick into o parsa", "idiot"))
            return
        }

        if(!json.has("error")) {
            var responseObj: T? = null
            if(Unit !is T) {
                responseObj = Gson().fromJson(jsonString, T::class.java)
            }
            done(responseObj)
        }else{
            val restError = Gson().fromJson(jsonString, RestErrorModel::class.java)
            error(restError)
        }
    }

    private fun getRestError(error: VolleyError): RestErrorModel{
        return RestErrorModel(0, error.message.orEmpty(), error.localizedMessage.orEmpty())
    }
}