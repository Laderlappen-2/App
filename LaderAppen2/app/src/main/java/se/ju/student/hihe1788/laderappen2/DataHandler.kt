package se.ju.student.hihe1788.laderappen2

import se.ju.student.hihe1788.laderappen2.models.RoutePagination

/**
 * An object that stores data.
 */
object DataHandler {
    private lateinit var routes: ArrayList<RouteModel>
    private lateinit var currentRoute: RouteModel

    /**
     * Returns all stored routes.
     * @return An ArrayList with all stored routes.
     */
    fun getRoutes(): ArrayList<RouteModel> {
        return routes
    }

    /**
     * Sets routes to be stored.
     * @param routes The routes to be stored.
     */
    fun setRoutes(routes: ArrayList<RouteModel>) {
        this.routes = routes
    }

    /**
     * Returns the current route.
     * @return The current route.
     */
    fun getCurrentRoute(): RouteModel {
        return currentRoute
    }

    /**
     * Sets the current route
     * @param newRoute The started route.
     */
    fun setCurrentRoute(newRoute: RouteModel) {
        currentRoute = newRoute
    }
}