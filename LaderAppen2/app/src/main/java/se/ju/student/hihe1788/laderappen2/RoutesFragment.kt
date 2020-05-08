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


class RoutesFragment : Fragment() {
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private var mRoutes: ArrayList<RouteModel> = ArrayList()
    private lateinit var progressBarView: View
    private lateinit var progressBar: ProgressBar

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
        progressBar = progressBarView.findViewById<ProgressBar>(R.id.progressBar)
        return view
    }

    override fun onStart() {
        super.onStart()
        // TODO Inte köra den varje gång man "backar" in i viewen, utan endast när man kommer in i den från menyknappen, antar jag
        progressBarView.visibility = View.VISIBLE
        progressBar.isEnabled = true
        RestHandler.getAllRoutes({
            mRoutes.clear()
            mRoutes.addAll(DataHandler.getRoutes())
            // TODO Ta bort efter progress bar har lagts till
            Toast.makeText(context, "Done", Toast.LENGTH_LONG).show()
            updateRecycleView()
            progressBarView.visibility = View.GONE
        }, { error ->
            // TODO Bättre felmeddelande
            Toast.makeText(context, "Error: " + error?.message, Toast.LENGTH_LONG).show()
            progressBarView.visibility = View.GONE
        })
    }

    fun updateRecycleView() {
        //get request to API
        mAdapter.notifyDataSetChanged()
    }
}
