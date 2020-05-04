package se.ju.student.hihe1788.laderappen2

import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class RecyclerAdapter(private val mRoutes: ArrayList<RouteModel>) : RecyclerView.Adapter<RecyclerAdapter.RouteHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return RouteHolder(inflatedView)
    }

    override fun getItemCount() = mRoutes.size

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val itemRoute = mRoutes[position]
        holder.bindRoute(itemRoute)
    }

    class RouteHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var mView: View = v
        private var mRoute: RouteModel? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //TODO: send in RouteModel as arg
            if (v?.findNavController()?.currentDestination?.id == R.id.routes_dest) {
                v.findNavController().navigate(R.id.routeItemFragment, null)
            }
        }

        fun bindRoute(route: RouteModel) {
            this.mRoute = route
            mView.routeTitle.text = "${mRoute?.id}"
        }

        companion object {
            private val ROUTE_KEY = "ROUTE"
        }
    }

}