package com.salik.nobrokerapp.adapter

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.salik.nobrokerapp.R
import com.salik.nobrokerapp.User


class Place(private val userList: ArrayList<User>) : RecyclerView.Adapter<Place.MyViewHolder>() {
    private var context: Context? = null;
    private lateinit var dbref : DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.order_recycler,
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
            Log.d("tag",currentitem.cord)
            holder.web.visibility= View.VISIBLE
            holder.ln.visibility=View.INVISIBLE
            holder.web.getSettings().setJavaScriptEnabled(true);
            holder.web.setWebViewClient(WebViewClient())
            holder.web.loadUrl(currentitem.cord)
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
        var web: WebView =itemView.findViewById(R.id.webview)
        var ln: LinearLayout =itemView.findViewById(R.id.ln1)


    }


}


