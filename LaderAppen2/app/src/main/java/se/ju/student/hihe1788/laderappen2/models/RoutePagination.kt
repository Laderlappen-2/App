package se.ju.student.hihe1788.laderappen2.models

import se.ju.student.hihe1788.laderappen2.RouteModel
import se.ju.student.hihe1788.laderappen2.models.interfaces.PaginationInterface

class RoutePagination(
    override val from: Int?,
    override val next: Int?,
    override val limit: Int,
    override val results: ArrayList<RouteModel>
): PaginationInterface<RouteModel>