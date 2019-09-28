package com.maurya91.recycleranimation.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maurya91.recycleranimation.R
import com.maurya91.recycleranimation.data.HomeData
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var adapter: HomeRecyclerAdapter
    val dataList = ArrayList<HomeData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        //create demo items
        createDemoItems()

        //setup recycler view
        adapter = HomeRecyclerAdapter()
        homeRecycler.layoutManager = LinearLayoutManager(this)
        homeRecycler.adapter = adapter

        //update items in adapter
        adapter.addItems(dataList)

        //for refresh
        fab.setOnClickListener {
            adapter.clearAll()
            adapter.addItems(dataList)
        }
        //add listener on remove button click
        adapter.setOnItemClickListener { v, item, position ->
            if (v.id == R.id.removeButton) {
                adapter.removeAt(position)
            }
        }
    }

    private fun createDemoItems(): ArrayList<HomeData> {
        for (i in 1..50) {
            dataList.add(HomeData(color = i))
        }
        return dataList

    }

}
