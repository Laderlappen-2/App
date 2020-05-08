package se.ju.student.hihe1788.laderappen2.util

import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import se.ju.student.hihe1788.laderappen2.RestMethodEnum
import java.nio.charset.Charset

class CustomRestRequest (
    method: RestMethodEnum,
    url: String,
    private val jsonString: String?,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
): StringRequest(method.value, url, listener, errorListener) {

    override fun getBody(): ByteArray {
        jsonString?.let {
            return it.toByteArray(Charset.forName("utf-8"))
        }
        return ByteArray(0)
    }

    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

    /*override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        val gson = Gson()
        response?.let { res ->
            var charset = HttpHeaderParser.parseCharset(response.headers)
            var jsonRes = gson.fromJson(String(response.data, Charset.forName(charset)), JsonObject::class.java)
            if(jsonRes.has("error")) {
                Response.error(Volley())
            }
        }
        return super.parseNetworkResponse(response)
    }*/
}