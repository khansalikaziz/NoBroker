package com.salik.nobrokerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Dashboard : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var ln1:LinearLayout
    lateinit var ln2:LinearLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var name=""

    lateinit var drawer:DrawerLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = Firebase.auth
        database = Firebase.database.reference

        val user=auth.currentUser
        val uuid= user?.uid

        ln1=findViewById(R.id.inc)
        ln2=findViewById(R.id.inc1)

        var property: View =ln1.findViewById(R.id.property)
        var order:View=ln2.findViewById(R.id.notice)
        var upload:View=ln1.findViewById(R.id.profile)
        var policy:View=ln2.findViewById(R.id.policy)
        policy.setOnClickListener {
            startActivity(Intent(this,PolicyActivity::class.java))
        }
        upload.setOnClickListener{
            Toast.makeText(applicationContext,"Upload Government Id",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,UserProfile::class.java))
        }
        property.setOnClickListener {
            startActivity(Intent(this,PropertyList::class.java))
        }
        order.setOnClickListener {
            startActivity(Intent(this,ViewOrder::class.java))
        }
        findViewById<TextView>(R.id.rep).setOnClickListener {
            startActivity(Intent(this,ViewOrder::class.java))
        }

        findViewById<ImageView>(R.id.imageView5).setOnClickListener {
            startActivity(Intent(this,PdfActivity::class.java))
        }



        if (uuid != null) {
            database.child("user").child(uuid).child("Name").get().addOnSuccessListener {
                name=it.value.toString()
                findViewById<TextView>(R.id.username).setText(name.toUpperCase())
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
            database.child("user").child(uuid).child("Email").get().addOnSuccessListener {
                val email=it.value.toString()
                findViewById<TextView>(R.id.uemail).setText(email)
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
            database.child("user").child(uuid).child("Name").get().addOnSuccessListener {
                val name=it.value.toString()
                findViewById<TextView>(R.id.uname).setText(name)
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        findViewById<ImageView>(R.id.imageView).setOnClickListener{

            startActivity(Intent(this,UserProfile::class.java))
        }
        findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            startActivity(Intent(this,PropertyList::class.java))
        }

        findViewById<ImageView>(R.id.imageView6).setOnClickListener {
            startActivity(Intent(this,PolicyActivity::class.java))
        }



        //Drawer
        var toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        var actionBarDrawerToggle = ActionBarDrawerToggle(this,drawer,toolbar,
            R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        //Drawer
    }
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.userprof ->startActivity(Intent(applicationContext,UserProfile::class.java))
            R.id.userRes -> {
                auth = Firebase.auth
                database = Firebase.database.reference
                val user=auth.currentUser
                val uuid= user?.uid
                if (uuid != null) {
                    database.child("user").child(uuid).child("govid").get().addOnSuccessListener {
                        val imgUrl=it.value.toString()
                        if(imgUrl.equals("0") || imgUrl.equals(null)){
                            Toast.makeText(applicationContext,"Upload Your Government Id First",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,UserProfile::class.java))
                        }else{
                        //pdf activity
                            startActivity(Intent(this,PdfActivity::class.java))
                        }

                    }.addOnFailureListener{
                        Toast.makeText(applicationContext,"Error...Try Again",Toast.LENGTH_LONG).show()
                    }
                }
            }
            R.id.userann -> {
                startActivity(Intent(this,AddPlaces::class.java))
            }
            R.id.userlog -> {

                startActivity(Intent(this,ViewAddPlaces::class.java))
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth = Firebase.auth
        database = Firebase.database.reference
        val user=auth.currentUser
        val uuid= user?.uid
        val id=item.itemId
        if(id==R.id.logout){
            Firebase.auth.signOut()
            startActivity(Intent(this,Login::class.java))
        }
        return true
    }

}