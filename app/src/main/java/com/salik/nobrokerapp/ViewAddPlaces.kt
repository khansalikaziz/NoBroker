package com.salik.nobrokerapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.salik.nobrokerapp.adapter.Place


class ViewAddPlaces : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbref1 : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userRecyclerview1 : RecyclerView
    private lateinit var userArrayList1 : ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_add_places)

        userRecyclerview1 = findViewById(R.id.userList4)
        userRecyclerview1.layoutManager = LinearLayoutManager(this)
        userRecyclerview1.setHasFixedSize(true)

        userArrayList1 = arrayListOf<User>()
        getUserData()
    }
    private fun getUserData() {
        auth = Firebase.auth
        val user = auth.currentUser
        var uuid= user?.uid

        dbref = FirebaseDatabase.getInstance().getReferenceFromUrl(
            Uri.parse("https://nobroker-e81fc-default-rtdb.firebaseio.com/user/"+uuid+"/property")
            .toString())


        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(User::class.java)

                        userArrayList1.add(user!!)

                    }

                    userRecyclerview1.adapter = Place(userArrayList1)


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}