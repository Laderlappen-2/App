package se.ju.student.hihe1788.laderappen2

class PaginationModel(val mFrom: Int?, val mNext: Int?, val mLimit: Int, val mResults: ArrayList<RouteModel>) {

    fun getRoutes(): ArrayList<RouteModel> {
        return mResults
    }
}