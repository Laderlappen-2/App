package se.ju.student.hihe1788.laderappen2

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CustomRestRequest (
    method: Int,
    url: String,
    private val jsonString: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
): StringRequest(method, url, listener, errorListener) {

    override fun getBody(): ByteArray {
        return (jsonString + "").toByteArray(Charset.forName("utf-8"))
    }

    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

}