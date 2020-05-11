package se.ju.student.hihe1788.laderappen2

import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import se.ju.student.hihe1788.laderappen2.util.RestHandler


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
            if (v?.findNavController()?.currentDestination?.id == R.id.routes_dest) {

                if (mRoute != null) {
                    RestHandler.getAllEventsForRouteWithId(mRoute!!.id, { route->
                        mRoute = route
                        val action = RoutesFragmentDirections.nextAction(mRoute!!)
                        v.findNavController().navigate(action)
                    }, { error ->
                        // TODO Bättre felmeddelande
                        println("ERROR: ${error?.message}")
                        Toast.makeText(MainActivity.mAppContext, "Error: " + error?.message, Toast.LENGTH_LONG).show()
                    })
                }
            }
        }

        fun bindRoute(route: RouteModel) {
            this.mRoute = route
            val text = MainActivity.mAppContext.getString(R.string.route_date,"${mRoute?.createdAt}.".substringBefore("G"))
            mView.routeTitle.text = text
        }

        companion object {
            private val ROUTE_KEY = "ROUTE"
        }
    }
}