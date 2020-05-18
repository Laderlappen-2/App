package se.ju.student.hihe1788.laderappen2

/**
 * A model for an error from the rest API.
 * @property statusCode The HTTP status code of the error
 * @property error The error title
 * @property message A descriptive error message
 */
class RestErrorModel(var statusCode: Int, val error: String, val message: String)