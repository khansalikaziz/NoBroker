package com.salik.nobrokerapp.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.salik.nobrokerapp.PaymentGateway
import com.salik.nobrokerapp.R
import com.salik.nobrokerapp.User
import com.salik.nobrokerapp.UserProfile


class MyAdapter(private val userList : ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var context: Context? = null;
    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var address=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recyler_item,
            parent,false)
        context=parent.context
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]


        holder.name.text = currentitem.Name
        holder.price.text = currentitem.Price
        holder.location.text = currentitem.location

        holder.about.text = currentitem.About
        holder.amen.text = currentitem.Amenities


        context?.let { Glide.with(it).load(currentitem.image).into(holder.img) }


        holder.cor.setOnClickListener {
            holder.web.visibility=View.VISIBLE
            holder.ln.visibility=View.INVISIBLE
            holder.web.getSettings().setJavaScriptEnabled(true);
            holder.web.setWebViewClient(WebViewClient())
            holder.web.loadUrl(currentitem.cord)
        }

        holder.call.setOnClickListener {
            val phoneNumber=currentitem.phone
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+phoneNumber)
            context?.startActivity(intent)
        }

        auth = Firebase.auth
        database = Firebase.database.reference
        val user = auth.currentUser
        var uuid= user?.uid
        if (uuid != null) {
            database.child("user").child(uuid).child("govid").get().addOnSuccessListener {
                address=it.value.toString()
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        holder.buy.setOnClickListener {
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("Are You Sure");
            builder?.setMessage("This will open payment gateway")
            builder?.setIcon(R.drawable.profile)

            if(address.equals("0")){
                Toast.makeText(context,"Upload your id proof",Toast.LENGTH_LONG).show()
                val intent = Intent(context, UserProfile::class.java)
                context?.startActivity(intent)
            }else {

                builder?.setPositiveButton("Yes"){dialogInterface, which ->
                    val intent = Intent(context, PaymentGateway::class.java)
                    intent.putExtra("price",currentitem.Price)
                    intent.putExtra("name",currentitem.Name)
                    intent.putExtra("location",currentitem.location)
                    intent.putExtra("about",currentitem.About)
                    intent.putExtra("amen",currentitem.Amenities)
                    intent.putExtra("cord",currentitem.cord)
                    intent.putExtra("image",currentitem.image)
                    intent.putExtra("phone",currentitem.phone)

                    context?.startActivity(intent)
                }
                builder?.setNegativeButton("No"){dialogInterface, which ->
                    Toast.makeText(context,"Payment Cancelled",Toast.LENGTH_LONG).show()
                }
                val alertDialog: AlertDialog? = builder?.create()
                alertDialog?.setCancelable(false)
                alertDialog?.show()


            }

        }

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.textView2)
        val price : TextView = itemView.findViewById(R.id.textView11)
        val location : TextView = itemView.findViewById(R.id.textView90)

        val about : TextView = itemView.findViewById(R.id.textView15)
        val amen : TextView = itemView.findViewById(R.id.textView9)
        val cor : Button = itemView.findViewById(R.id.button3)

        val img : ImageView = itemView.findViewById(R.id.imageView8)
        var web:WebView=itemView.findViewById(R.id.webview)
        var ln:LinearLayout=itemView.findViewById(R.id.ln1)

        var call:Button=itemView.findViewById(R.id.button)

        var buy:Button=itemView.findViewById(R.id.button2)
    }


}


