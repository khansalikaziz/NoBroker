package com.salik.nobrokerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

class PaymentGateway : AppCompatActivity(),PaymentResultListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var address=""
    var temp="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment_gateway)

        var price=intent.getStringExtra("price")
        var amount=price?.split(" ")?.toTypedArray()
        temp= amount?.get(0).toString()
        //other data access

        //other data access


            Checkout.preload(this@PaymentGateway)
            val checkout=Checkout()
            checkout.setKeyID("rzp_test_1OYGeaOBeM06uG")
            try {

                val options=JSONObject()
                options.put("name","Salik Aziz Khan")
                options.put("description","This is des")
                options.put("image",R.drawable.lpulogo)
                options.put("theme.color","#FF03DAC5")
                options.put("currency","INR")
                options.put("amount",temp.toInt()*10000)

                val prefill = JSONObject()
                prefill.put("email","boltuix@gmail.com")   // put email
                prefill.put("contact","123457891")    // put mobile number
                options.put("prefill",prefill)

                val retryObj=JSONObject()
                retryObj.put("enabled",true)
                retryObj.put("max_count",4)

                options.put("retry",retryObj)

                checkout.open(this@PaymentGateway,options)

            }catch (e:Exception){
                Toast.makeText(applicationContext,"Error in Payment : "+e.message,Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }




    }

    override fun onPaymentSuccess(p0: String?) {
        //data
        var names=intent.getStringExtra("name")
        var price=intent.getStringExtra("price")
        var location=intent.getStringExtra("location")
        var about=intent.getStringExtra("about")
        var amen=intent.getStringExtra("amen")
        var cord=intent.getStringExtra("cord")
        var image=intent.getStringExtra("image")
        var phone=intent.getStringExtra("phone")
        //data
        //sending on realtime db
        auth = Firebase.auth
        database = Firebase.database.reference
        val user = auth.currentUser
        var uuid= user?.uid
        if (uuid != null && names!=null) {
            database.child("user").child(uuid).child("purchase").child(names).child("Name").setValue(names)
            database.child("user").child(uuid).child("purchase").child(names).child("location").setValue(location)
            database.child("user").child(uuid).child("purchase").child(names).child("About").setValue(about)
            database.child("user").child(uuid).child("purchase").child(names).child("Amenities").setValue(amen)
            database.child("user").child(uuid).child("purchase").child(names).child("cord").setValue(cord)
            database.child("user").child(uuid).child("purchase").child(names).child("image").setValue(image)
            database.child("user").child(uuid).child("purchase").child(names).child("phone").setValue(phone)
            database.child("user").child(uuid).child("purchase").child(names).child("Price").setValue(price)
            startActivity(Intent(applicationContext,ViewOrder::class.java))
        }
        Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(applicationContext,"Failure",Toast.LENGTH_LONG).show()
    }


}