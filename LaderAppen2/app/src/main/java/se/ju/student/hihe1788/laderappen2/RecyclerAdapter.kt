package se.ju.student.hihe1788.laderappen2

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import se.ju.student.hihe1788.laderappen2.util.RestHandler

/**
 * An RecyclerAdapter for the RecyclerView in RoutesFragments
 * @see RoutesFragment
 * @see OFFICIAL_DOC_ANDROID_DEVELOPER
 */
class RecyclerAdapter(private val mRoutes: ArrayList<RouteModel>) : RecyclerView.Adapter<RecyclerAdapter.RouteHolder>() {

    /**
     * Returns the view for a route item in RoutesFragment.
     * @see R.layout.recyclerview_item_row
     * @see RoutesFragment
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return RouteHolder(inflatedView)
    }

    /**
     * Returns the numbers of routes that are to be displayed in RouteFragment.
     * @see RoutesFragment
     * @return Number of routes in mRoute.
     */
    override fun getItemCount() = mRoutes.size

    /**
     * Binds each view item with a route.
     * @param holder The RouteHolder
     * @param position The position in the recyclerView in RoutesFragment
     * @see RoutesFragment
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val itemRoute = mRoutes[position]
        holder.bindRoute(itemRoute)
    }

    /**
     * Represents each route item in RouteFragment
     * @param v The view for a route item in the recyclerView in RoutesFragment.
     * @see R.layout.recyclerview_item_row
     * @see RoutesFragment
     */
    class RouteHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var mView: View = v
        private var mRoute: RouteModel? = null

        /**
         * Sets an on click listener to a route item in RoutesFragment.
         * @see RoutesFragment
         */
        init {
            v.setOnClickListener(this)
        }

        /**
         * Called when a route item in RoutesFragment have been pressed.
         * Fetches all positions for the route item selected and navigates to RouteItemFragment.
         * @param v The view for an Item in the recyclerView in RoutesFragment.
         * @see RoutesFragment
         * @see RestHandler.getAllEventsForRouteWithId
         * @see RouteItemFragment
         */
        override fun onClick(v: View?) {
            if (v?.findNavController()?.currentDestination?.id == R.id.routes_dest) {

                if (mRoute != null) {
                    RestHandler.getAllEventsForRouteWithId(mRoute!!.id, { route->
                        mRoute = route
                        val action = RoutesFragmentDirections.nextAction(mRoute!!)
                        v.findNavController().navigate(action)
                    }, { error ->
                        // TODO Bättre felmeddelande
                        Toast.makeText(MainActivity.mContext, "Error: " + error?.message, Toast.LENGTH_LONG).show()
                    })
                }
            }
        }

        /**
         * Binds each element in recyclerview_item_row with mRoute
         * @param route
         * @see R.layout.recyclerview_item_row
         * @see OFFICIAL_DOC_ANDROID_DEVELOPER
         */
        fun bindRoute(route: RouteModel) {
            this.mRoute = route
            val text = MainActivity.mContext.getString(R.string.route_date,"${mRoute?.createdAt}.".substringBefore("G"))
            mView.routeTitle.text = text
        }
    }
}