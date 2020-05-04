package se.ju.student.hihe1788.laderappen2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if (mRoutes.size == 0) {
            //TODO: get array from Data handler?
            val pList = ArrayList<PointModel>()
            val cList = ArrayList<PointModel>()
            mRoutes.add(RouteModel(1, pList, cList))
            mRoutes.add(RouteModel(2, pList, cList))
            updateRecycleView()
        }
    }

    fun updateRecycleView() {
        //get request to API
        mAdapter.notifyDataSetChanged()
    }
}
