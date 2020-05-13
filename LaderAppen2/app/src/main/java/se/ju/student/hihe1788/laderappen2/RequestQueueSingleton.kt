package se.ju.student.hihe1788.laderappen2

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
/**
 * A class that creates a volley request queue.
 * @property context The application context to work with
 * @see OFFICIAL_DOC_ANDROID_DEVELOPER
 */
class RequestQueueSingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: RequestQueueSingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RequestQueueSingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    /**
     * The volley request queue.
     * Instantiates and sets a new instance if null
     */
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    /**
     * Add a request to the volley request queue
     * @param req The volley string request object
     */
    fun addToRequestQueue(req: StringRequest) {
        requestQueue.add(req)
    }
}