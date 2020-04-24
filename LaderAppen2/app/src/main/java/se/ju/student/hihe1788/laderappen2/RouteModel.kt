package se.ju.student.hihe1788.laderappen2

class RouteModel(val id: Int, val positionEvent: ArrayList<PointModel>?, val collisionAvoidanceEvent: ArrayList<PointModel>?) {

    fun getPositions(): ArrayList<PointModel> {
        var tempPositions: ArrayList<PointModel> = ArrayList()
        positionEvent?.let { tempPositions = it }

        return tempPositions
    }

    fun getCollisonAvoidances(): ArrayList<PointModel> {
        var tempCA: ArrayList<PointModel> = ArrayList()
        collisionAvoidanceEvent?.let { tempCA = it }

        return tempCA
    }
}

class PointModel(val positionX: Int, val positionY: Int, val isCollisionAvoidance: Boolean?) {}
