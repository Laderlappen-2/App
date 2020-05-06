package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RoutesFragment : Fragment() {
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private var mRoutes: ArrayList<RouteModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.routes_fragment, container, false)
        mLinearLayoutManager = LinearLayoutManager(MainActivity.mAppContext)
        mRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        mRecyclerView.layoutManager = mLinearLayoutManager
        mAdapter = RecyclerAdapter(mRoutes)
        mRecyclerView.adapter = mAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        // TODO Inte köra den varje gång man "backar" in i viewen, utan endast när man kommer in i den från menyknappen, antar jag
        RestHandler.getAllRoutes({
            mRoutes.clear()
            mRoutes.addAll(DataHandler.getRoutes())
            // TODO Ta bort efter progress bar har lagts till
            Toast.makeText(context, "Done", Toast.LENGTH_LONG).show()
            updateRecycleView()
        }, { error ->
            // TODO Bättre felmeddelande
            Toast.makeText(context, "Error: " + error?.message, Toast.LENGTH_LONG).show()
        })
    }

    fun updateRecycleView() {
        //get request to API
        mAdapter.notifyDataSetChanged()
    }
}
