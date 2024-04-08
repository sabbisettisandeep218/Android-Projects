package com.example.demo_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity:AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val name=intent.getStringExtra("Name")
        val data="Hello ${name}! Welcome to Mvgr College of Engineering"
        val textView=findViewById<TextView>(R.id.text2)
        textView.text=data

        val goback=findViewById<Button>(R.id.gobackBtn)
        goback.setOnClickListener{
            finish()
        }

        val sharedPref=getSharedPreferences("mypref", MODE_PRIVATE)
        val editor= sharedPref.edit()

        val savbtn=findViewById<Button>(R.id.savbtn)
        savbtn.setOnClickListener{
            val dob=findViewById<EditText>(R.id.dobid).text.toString()
            val addr=findViewById<EditText>(R.id.Addrid).text.toString()

            editor.putString("Dob",dob).apply()
            editor.putString("Address",addr).apply()
        }
        val loadBtn=findViewById<Button>(R.id.loadbtn)
        loadBtn.setOnClickListener{
            val dob=sharedPref.getString("Dob",null)
            val addr=sharedPref.getString("Address",null)

            findViewById<EditText>(R.id.dobid).setText(dob)
            findViewById<EditText>(R.id.Addrid).setText(addr)
        }



    }
}