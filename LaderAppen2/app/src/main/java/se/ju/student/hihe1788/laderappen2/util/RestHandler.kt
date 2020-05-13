package se.ju.student.hihe1788.laderappen2.util

import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.JsonObject
import se.ju.student.hihe1788.laderappen2.*
import se.ju.student.hihe1788.laderappen2.models.RoutePagination
import java.nio.charset.Charset

/**
 * An object that handles all HTTP requests to the REST API.
 */
object RestHandler {
    private const val BASE_URL = "https://laderlappen-2-rest-api.herokuapp.com/v1"
    private const val URI_DRIVINGSESSIONS = "/drivingsessions"
    private const val URI_EVENTS = "/events"
    private val gson = Gson()

    /**
     * Sends a GET request to https://laderlappen-2-rest-api.herokuapp.com/v1/drivingsessions
     * Fetches all routes.
     * @param successCallback Callback if request is a success
     * @param errorCallback Callback if request returns an error
     */
    fun getAllRoutes(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$BASE_URL$URI_DRIVINGSESSIONS"

        val jsonObjectRequest =
            CustomRestRequest(
                RestMethodEnum.GET,
                url,
                null,
                Response.Listener { jsonStringRes ->
                    parseStringResponse<RoutePagination>(
                        jsonStringRes
                    ) { pagination ->
                        pagination?.let {
                            // TODO Do not use DataHandler, instead return result in "successCallback()"
                            DataHandler.setRoutes(
                                it.results
                            )
                        }
                        successCallback()
                    }
                },
                Response.ErrorListener { error ->
                    parseErrorResponse(
                        error,
                        errorCallback
                    )
                })
        RequestQueueSingleton.getInstance(
            MainActivity.mAppContext
        ).addToRequestQueue(jsonObjectRequest)
    }

    /**
     * Sends a GET request to https://laderlappen-2-rest-api.herokuapp.com/v1/drivingsessions/:id
     * Fetches all events for the route.
     * @param id The id for the route the events belong to.
     * @param successCallback Callback if request is a success
     * @param errorCallback Callback if request returns an error
     */
    fun getAllEventsForRouteWithId(id: Int, successCallback: (route: RouteModel) -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$BASE_URL$URI_DRIVINGSESSIONS/$id"
        val jsonObjectRequest =
            CustomRestRequest(
                RestMethodEnum.GET,
                url,
                null,
                Response.Listener { jsonStringRes ->
                    parseStringResponse<RouteModel>(
                        jsonStringRes
                    ) { route ->
                        route?.let {
                            successCallback(it)
                        }
                    }
                },
                Response.ErrorListener { error ->
                    parseErrorResponse(
                        error,
                        errorCallback
                    )
                }
            )
        RequestQueueSingleton.getInstance(
            MainActivity.mAppContext
        ).addToRequestQueue(jsonObjectRequest)
    }

    /**
     * Sends a Post request to https://laderlappen-2-rest-api.herokuapp.com/v1/drivingsessions
     * Creates a new route and fetches it.
     * @param successCallback Callback if request is a success
     * @param errorCallback Callback if request returns an error
     */
    fun createDrivingSession(successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$BASE_URL$URI_DRIVINGSESSIONS"

        val jsonObjectRequest =
            CustomRestRequest(
                RestMethodEnum.POST,
                url,
                null,
                Response.Listener() { response ->
                    parseStringResponse<RouteModel>(
                        response
                    ) { parsedResponse ->
                        parsedResponse?.let {
                            DataHandler.setCurrentRoute(parsedResponse)
                        }
                    }
                },
                Response.ErrorListener { error ->
                    parseErrorResponse(
                        error,
                        errorCallback
                    )
                }
            )
        RequestQueueSingleton.getInstance(
            MainActivity.mAppContext
        ).addToRequestQueue(jsonObjectRequest)
    }

    /**
     * Sends a POST request to https://laderlappen-2-rest-api.herokuapp.com/v1/drivingsessions/:id/events
     * Posts all events for a route.
     * @param route The route the events belong to.
     * @param successCallback Callback if request is a success
     * @param errorCallback Callback if request returns an error
     */
    fun createBatchEvents(route: RouteModel, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$BASE_URL$URI_DRIVINGSESSIONS/${route.id}$URI_EVENTS"

        val jsonString = Klaxon().toJsonString(route.events)

        val jsonArrayRequest =
            CustomRestRequest(
                RestMethodEnum.POST,
                url,
                jsonString,
                Response.Listener { responseString ->
                    parseStringResponse<JsonObject>(
                        responseString
                    ) { jsonObject ->
                        successCallback()
                    }
                },
                Response.ErrorListener { error ->
                    parseErrorResponse(
                        error,
                        errorCallback
                    )
                })

        RequestQueueSingleton.getInstance(
            MainActivity.mAppContext
        ).addToRequestQueue(jsonArrayRequest)
    }

    /**
     * Sends a DELETE request to https://laderlappen-2-rest-api.herokuapp.com/v1/drivingsessions/:id
     * Deletes a route
     * @param id The id for the route that should be deleted.
     * @param successCallback Callback if request is a success
     * @param errorCallback Callback if request returns an error
     */
    fun deleteRouteById(id: Int, successCallback: () -> Unit, errorCallback: (error: RestErrorModel?) -> Unit) {
        val url = "$BASE_URL$URI_DRIVINGSESSIONS/$id"

        val jsonObjectRequest =
            CustomRestRequest(
                RestMethodEnum.DELETE,
                url,
                null,
                Response.Listener {
                    successCallback()
                },
                Response.ErrorListener { error ->
                    parseErrorResponse(
                        error,
                        errorCallback
                    )
                }
            )

        RequestQueueSingleton.getInstance(
            MainActivity.mAppContext
        ).addToRequestQueue(jsonObjectRequest)
    }

    /**
     * Parses the response string.
     * @param jsonString The response string
     * @param done callback thata returnes the parsed object
     */
    private inline fun<reified T> parseStringResponse(jsonString: String, done: (T?) -> Unit) {
        val json = gson.fromJson(jsonString, JsonObject::class.java)

        if(json == null) {
            error(
                RestErrorModel(
                    0,
                    "Invalid json",
                    "JSON string is not valid"
                )
            )
            return
        }

        val responseObj: T? = gson.fromJson(jsonString, T::class.java)
        done(responseObj)
    }

    /**
     * Parses the an error response.
     * @param error The volley error
     * @param done callback thata returnes the parsed error message
     */
    private fun parseErrorResponse(error: VolleyError, done: (RestErrorModel) -> Unit) {
        try {
            val charset = HttpHeaderParser.parseCharset(error.networkResponse.headers)
            val jsonString = String(error.networkResponse.data, Charset.forName(charset))
            val restError = gson.fromJson(jsonString, RestErrorModel::class.java)
            restError.statusCode = error.networkResponse.statusCode
            done(restError)
        }catch(ex: Exception) {
            // Any exception would be catched here, and then returned to the calling function
            val restError = RestErrorModel(
                0,
                ex.message.orEmpty(),
                ex.localizedMessage.orEmpty()
            )
            error.networkResponse?.let {
                restError.statusCode = error.networkResponse.statusCode
            }
            done(restError)
        }
    }
}