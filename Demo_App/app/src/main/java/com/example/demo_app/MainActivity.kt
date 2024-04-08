package com.example.demo_app

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.documentfile.provider.DocumentFile
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    val channelId = 0
    private val channelName = "Channel Name"
    private val notificationId = 1
    private lateinit var toggle: ActionBarDrawerToggle
    private val REQUEST_PICK_FILE = 100

    companion object {
        private const val Camer_permission_id = 101
    }

    private val requestToFile = 2

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val url = intent.getStringExtra(DocumentsContract.EXTRA_INITIAL_URI)
        findViewById<TextView>(R.id.tv).text = url


        //Intent and start Activity and passing the data
        val tonxtActBtn = findViewById<Button>(R.id.tonxtBtn)
        tonxtActBtn.setOnClickListener {
            val name = findViewById<EditText>(R.id.name).text.toString()
            val intent = Intent(this, SecondActivity::class.java).also { data ->
                data.putExtra("Name", name)
            }
            startActivity(intent)
        }

        //ToolBar Menu
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //Toolbar act as Action bar

        //Slidable Menu
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLay)
        val navview = findViewById<NavigationView>(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> Toast.makeText(this, "About Option is Selected", Toast.LENGTH_SHORT)
                    .show()

                R.id.faculty -> Toast.makeText(
                    this,
                    "Faculty Option is Selected",
                    Toast.LENGTH_SHORT
                )
                    .show()

                R.id.Academics -> Toast.makeText(
                    this,
                    "Academics Option is Selected",
                    Toast.LENGTH_SHORT
                )
                    .show()

                R.id.mid -> Toast.makeText(this, "Mid Exams Option is Selected", Toast.LENGTH_SHORT)
                    .show()

                R.id.sem -> Toast.makeText(this, "Semester Option is Selected", Toast.LENGTH_SHORT)
                    .show()

            }
            true

        }

        //Notifications
        createNotificationChannel()
        val notifyButton = findViewById<Button>(R.id.NotifyBtn)
        notifyButton.setOnClickListener {
            showNotification()
        }

        //Swippable content
        val images = listOf(
            R.drawable.mvgr_logo,
            R.drawable.mvgr_admin,
            R.drawable.mvgr_library,
            R.drawable.mvgr_gate
        )
        val adapter = ViewPageAdapter(images)
        val viewpager = findViewById<ViewPager2>(R.id.viewPager)
        viewpager.adapter = adapter

        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //TabLayout with PageViewer
        val tabLayout = findViewById<TabLayout>(R.id.tablayout)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = "Tab${position + 1}"
        }.attach()


        //Fragments with BottomNavigation
        val frag1 = Chat_Fragment1()
        val frag2 = Status_Fragment2()
        val frag3 = Call_Fragment3()

        setCurrentFragment(frag1)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chatFragId -> setCurrentFragment(frag1)
                R.id.statusFragId -> setCurrentFragment(frag2)
                R.id.callFragId -> setCurrentFragment(frag3)
            }
            true
        }


        //Spinner
        val spinView = findViewById<Spinner>(R.id.spinnerid)
        spinView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                itemview: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "You selected ${itemview?.getItemAtPosition(position).toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //No item is selected
            }
        }

        //Camera Permissions
        val camBtn = findViewById<Button>(R.id.CamBtn)
        camBtn.setOnClickListener {
            checkPermissions(Manifest.permission.CAMERA, Camer_permission_id)
        }

        //Broadcast Message for  Airplane Mode
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(myBroadcastReciever(), it, RECEIVER_NOT_EXPORTED)
        }


    }

    //ToolBar Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.item_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeIc -> Toast.makeText(this, "Home is selected", Toast.LENGTH_SHORT).show()
            R.id.favIc -> Toast.makeText(this, "Favourite is selected", Toast.LENGTH_SHORT).show()
            R.id.setIc -> Toast.makeText(this, "Setting is selected", Toast.LENGTH_SHORT).show()

            //Slidable Menu-Navigation Pane
            else -> {
                if (toggle.onOptionsItemSelected(item)) {
                    return true
                }
                return super.onOptionsItemSelected(item)
            }
        }
        return true

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mychannel = NotificationChannel(
                channelId.toString(),
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val myNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            myNotificationManager.createNotificationChannel(mychannel)
        }
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, channelId.toString())
            .setContentTitle("Demo App")
            .setContentText("This is demo app notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragLayout, fragment).commit()
    }

    //Camera Permissions
    private fun checkPermissions(permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()
            } else {

                requestPermissions(arrayOf(permission), 101)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray //Request is cancelled ,array is empty
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Camer_permission_id) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission is Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission is Not Granted", Toast.LENGTH_SHORT).show()

            }
        }
    }


}

