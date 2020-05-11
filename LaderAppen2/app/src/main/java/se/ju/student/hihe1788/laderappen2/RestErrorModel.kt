package se.ju.student.hihe1788.laderappen2

/**
 * A model for an error from the rest API.
 */
class RestErrorModel(var statusCode: Int, val error: String, val message: String) { }