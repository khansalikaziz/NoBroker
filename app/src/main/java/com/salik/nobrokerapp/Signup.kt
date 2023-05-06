package com.salik.nobrokerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    lateinit var emailText: EditText
    lateinit var passwordText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth
        database = Firebase.database.reference

        emailText=findViewById(R.id.emailBox)
        passwordText=findViewById(R.id.passwordBox)
//        imageView=findViewById(R.id.profile_image)

        var register: Button =findViewById(R.id.regBtn)
//
//        imageView.setOnClickListener{
//
//        }

        register.setOnClickListener {
            var email=emailText.text.toString()
            var password=passwordText.text.toString()
            var name=findViewById<EditText>(R.id.namebox).text.toString()
            var gov_id=findViewById<EditText>(R.id.regbox).text.toString()
            var add=findViewById<EditText>(R.id.about).text.toString()

            //Register
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !gov_id.isEmpty() && !add.isEmpty() && password.length>5){
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            var uuid= user?.uid

                            //Adding Value in database
                            if (uuid != null) {
                                database.child("user").child(uuid).child("Email").setValue(email)
                                database.child("user").child(uuid).child("Password").setValue(password)
                                database.child("user").child(uuid).child("Name").setValue(name)
                                database.child("user").child(uuid).child("Gov").setValue(gov_id)
                                database.child("user").child(uuid).child("Address").setValue(add)

                                database.child("user").child(uuid).child("govid").setValue("0")

//                                database.child("user").child(uuid).child("resume").setValue("0")
//
//                                //Drive Data
//                                database.child("user").child(uuid).child("Drive").child("airbus").setValue("0")
//                                database.child("user").child(uuid).child("Drive").child("nyala").setValue("0")
//                                database.child("user").child(uuid).child("Drive").child("altair").setValue("0")
//                                database.child("user").child(uuid).child("Drive").child("opentext").setValue("0")
//                                database.child("user").child(uuid).child("Drive").child("bajaj").setValue("0")

                            }

                            //Adding Value in database

                            startActivity(Intent(this,Login::class.java))
                            Toast.makeText(baseContext, "SignUp Successfull",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(baseContext, "Authentication failed. Try Again",
                                Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(baseContext, "Fill all inputs",
                            Toast.LENGTH_SHORT).show()
                    }

                }
            //Register
        }

        findViewById<TextView>(R.id.loginBtn).setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
//    public override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            startActivity(Intent(this,Dashboard::class.java))
//        }
//    }
}