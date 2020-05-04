package se.ju.student.hihe1788.laderappen2.models.interfaces

interface PaginationInterface<T> {
    val from: Int?
    val next: Int?
    val limit: Int
    val results: ArrayList<T>
}