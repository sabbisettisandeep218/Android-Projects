package com.example.demo_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class myBroadcastReciever:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val enableairplanemode =intent?.getBooleanExtra("airplanemode",false)?:return
        if (enableairplanemode){
            Toast.makeText(context,"Airplane Mode Enable..Thank You",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Airplane Mode Disable",Toast.LENGTH_SHORT).show()
        }
    }
}