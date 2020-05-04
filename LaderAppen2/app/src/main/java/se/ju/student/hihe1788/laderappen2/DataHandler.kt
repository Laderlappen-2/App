package se.ju.student.hihe1788.laderappen2

import se.ju.student.hihe1788.laderappen2.models.RoutePagination

object DataHandler {
    private lateinit var routes: ArrayList<RouteModel>
    private lateinit var currentRoute: RouteModel

    fun getRoutes(): ArrayList<RouteModel> {
        return routes
    }

    fun setRoutes(routes: ArrayList<RouteModel>) {
        this.routes = routes
    }

    fun getCurrentRoute(): RouteModel {
        return currentRoute
    }

    fun setCurrentRoute(newRoute: RouteModel) {
        currentRoute = newRoute
    }
}