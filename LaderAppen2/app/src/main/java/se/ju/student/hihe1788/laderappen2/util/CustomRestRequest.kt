package se.ju.student.hihe1788.laderappen2.util

import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import se.ju.student.hihe1788.laderappen2.RestMethodEnum
import java.nio.charset.Charset

/**
 * Custom volley request to send optional json strings, which then returns the response
 * @property method The method to use with the request
 * @property url Where to send the request
 * @property jsonString The json body to send with the request
 * @property listener Response listener function
 * @property errorListener Error listener function
 */
class CustomRestRequest (
    method: RestMethodEnum,
    url: String,
    private val jsonString: String?,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
): StringRequest(method.value, url, listener, errorListener) {

    /**
     * Returns the response body as a byte array
     */
    override fun getBody(): ByteArray {
        jsonString?.let {
            return it.toByteArray(Charset.forName("utf-8"))
        }
        return ByteArray(0)
    }

    /**
     * Returns the default body content type
     */
    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }
}