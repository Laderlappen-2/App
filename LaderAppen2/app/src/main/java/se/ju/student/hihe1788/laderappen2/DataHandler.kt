package se.ju.student.hihe1788.laderappen2

object DataHandler {
    private lateinit var pagination: PaginationModel

    fun getPagination(): PaginationModel {
        return pagination
    }

    fun setPagination(newPagination: PaginationModel) {
        pagination = newPagination
    }
}