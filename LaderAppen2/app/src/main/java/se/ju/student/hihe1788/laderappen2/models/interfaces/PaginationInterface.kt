package se.ju.student.hihe1788.laderappen2.models.interfaces
/**
 * An interface for pagination.
 */
interface PaginationInterface<T> {
    val from: Int?
    val next: Int?
    val limit: Int
    val results: ArrayList<T>
}