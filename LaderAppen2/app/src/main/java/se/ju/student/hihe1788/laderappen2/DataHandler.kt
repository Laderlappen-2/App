package se.ju.student.hihe1788.laderappen2

object DataHandler {
    private lateinit var pagination: PaginationModel
    private lateinit var currentRoute: RouteModel

    fun getPagination(): PaginationModel {
        return pagination
    }

    fun setPagination(newPagination: PaginationModel) {
        pagination = newPagination
    }

    fun getCurrentRoute(): RouteModel {
        return currentRoute
    }

    fun setCurrentRoute(newRoute: RouteModel) {
        currentRoute = newRoute
    }
}