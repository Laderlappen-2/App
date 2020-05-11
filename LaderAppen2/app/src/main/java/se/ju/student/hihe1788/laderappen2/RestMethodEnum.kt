package se.ju.student.hihe1788.laderappen2

/**
 * A class that holds enums for http requests.
 */
enum class RestMethodEnum(val value: Int) {
    DEPRECATED_GET_OR_POST(-1),
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    HEAD(4),
    OPTIONS(5),
    TRACE(6),
    PATCH(7)
}