package com.example.rv_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RvAdapter
    private lateinit var dbhelper: DatabaseHelper
    private val rvItems = mutableListOf<Rvdata>()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbhelper = DatabaseHelper(this)

        val rv = findViewById<RecyclerView>(R.id.rv)
        adapter = RvAdapter(rvItems, dbhelper)
        rv.adapter = adapter //setting adapter with recycler view
        rv.layoutManager =
            LinearLayoutManager(this) //we are creating the linearLayout - where the data added vertically


        val addtask = findViewById<Button>(R.id.addbtn)
        addtask.setOnClickListener {
            val taskDesc = findViewById<EditText>(R.id.editText).text.toString()
            if (taskDesc.isNotEmpty()) {
                val task = Rvdata(taskDesc,false)
                dbhelper.insertData(task)
                rvItems.add(task)
                adapter.notifyItemInserted(rvItems.size - 1)
                findViewById<EditText>(R.id.editText).text.clear()
            } else {
                Toast.makeText(this, "Enter the Task", Toast.LENGTH_SHORT).show()
            }
        }

        val delAll = findViewById<Button>(R.id.delAllbtn)
        delAll.setOnClickListener {
            rvItems.clear()
            dbhelper.deleteAllData()
            adapter.notifyDataSetChanged()

        }
        val loadtasks = findViewById<Button>(R.id.getbtn)
        loadtasks.setOnClickListener {
            Log.d("MainActivity", "Get button clicked")
            rvItems.clear()
            rvItems.addAll(dbhelper.getData())
            adapter.notifyDataSetChanged()
            Log.d("MainActivity", "Data loaded from database: ${rvItems.size} items")
        }

        /*val rvItems=mutableListOf<Rvdata>()
        rvItems.add(Rvdata("Fearful",false))
        rvItems.add(Rvdata("Happy",false))
        rvItems.add(Rvdata("Excited",false))
        rvItems.add(Rvdata("Thinking",false))
        */
    }


}