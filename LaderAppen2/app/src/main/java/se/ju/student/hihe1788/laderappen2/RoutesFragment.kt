package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.ju.student.hihe1788.laderappen2.util.RestHandler

/**
 * This fragment displays routes in a RecyclerView which is a typ of list.
 * */
class RoutesFragment : Fragment() {
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private var mRoutes: ArrayList<RouteModel> = ArrayList()
    private lateinit var progressBarView: View
    private lateinit var progressBar: ProgressBar

    /**
     * Override function that returns the view for RoutesFragment and sets up a RecyclerView with a recycler adapter.
     * @see R.layout.routes_fragment
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.routes_fragment, container, false)
        mLinearLayoutManager = LinearLayoutManager(MainActivity.mAppContext)
        mRecyclerView = view.findViewById<RecyclerView>(R.id.routesFragmentRecyclerView)
        mRecyclerView.layoutManager = mLinearLayoutManager
        mAdapter = RecyclerAdapter(mRoutes)
        mRecyclerView.adapter = mAdapter

        // Progress bar
        progressBarView = view.findViewById(R.id.routesFragmentProgress)
        progressBar = progressBarView.findViewById(R.id.progressBar)
        progressBar.isEnabled = true
        return view
    }

    /**
     * Override function that fetches the routes from the database and calls updateRecycleView().
     * @see updateRecycleView
     * @see OFFICIAL_DOC_ANDROID_DEVELOPER
     */
    override fun onStart() {
        super.onStart()
        // TODO Inte köra den varje gång man "backar" in i viewen, utan endast när man kommer in i den från menyknappen, antar jag
        progressBarView.visibility = View.VISIBLE
        RestHandler.getAllRoutes({
            mRoutes.clear()
            mRoutes.addAll(DataHandler.getRoutes())
            updateRecycleView()
            progressBarView.visibility = View.GONE
        }, { error ->
            // TODO Bättre felmeddelande
            Toast.makeText(context, "Error: " + error?.message, Toast.LENGTH_LONG).show()
            progressBarView.visibility = View.GONE
        })
    }

    /**
     * Notifies the connected recycler adapter that changes have been made to the recyclerView.
     */
    fun updateRecycleView() {
        mAdapter.notifyDataSetChanged()
    }
}
