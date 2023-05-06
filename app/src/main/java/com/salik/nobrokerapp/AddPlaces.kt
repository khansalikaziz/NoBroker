package com.salik.nobrokerapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AddPlaces : AppCompatActivity() {
    var imageUrl=""
    lateinit var progress: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var name:EditText
    lateinit var about:EditText
    lateinit var area:EditText
    lateinit var price:EditText
    lateinit var map:EditText
    lateinit var address:EditText
    lateinit var phone:EditText
    lateinit var img:ImageView
    lateinit var btn:Button
    val GALLERY_REQUEST_CODE=4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_places)

        auth = Firebase.auth
        database = Firebase.database.reference

        name=findViewById(R.id.namebox)
        about=findViewById(R.id.regbox)
        area=findViewById(R.id.emailBox)
        price=findViewById(R.id.passwordBox)
        map=findViewById(R.id.about)
        address=findViewById(R.id.addr)
        phone=findViewById(R.id.phones)
        img=findViewById(R.id.imageView7)
        btn=findViewById(R.id.regBtn)





        btn.setOnClickListener{

            var names=name.text.toString().trim()
            var abouts=about.text.toString().trim()
            var areas=area.text.toString().trim()
            var prices=price.text.toString().trim()
            var maps=map.text.toString().trim()
            var adds=address.text.toString().trim()
            var phones=phone.text.toString().trim()



            val user = auth.currentUser
            var uuid= user?.uid
            if (uuid != null && imageUrl.length>0 && names.length>0 && abouts.length>0 && areas.length>0 && prices.length>0 && maps.length>0 && adds.length>0 && phones.length>0) {
                //property data push
                database.child("property").child(names).child("About").setValue(abouts)
                database.child("property").child(names).child("Amenities").setValue(areas)
                database.child("property").child(names).child("Name").setValue(names)
                database.child("property").child(names).child("Price").setValue(prices)
                database.child("property").child(names).child("cord").setValue(maps)
                database.child("property").child(names).child("image").setValue(imageUrl)
                database.child("property").child(names).child("location").setValue(adds)
                database.child("property").child(names).child("phone").setValue(phones.toLong())

                //myplace data push
                database.child("user").child(uuid).child("property").child(names).child("About").setValue(abouts)
                database.child("user").child(uuid).child("property").child(names).child("Amenities").setValue(areas)
                database.child("user").child(uuid).child("property").child(names).child("Name").setValue(names)
                database.child("user").child(uuid).child("property").child(names).child("Price").setValue(prices)
                database.child("user").child(uuid).child("property").child(names).child("cord").setValue(maps)
                database.child("user").child(uuid).child("property").child(names).child("image").setValue(imageUrl)
                database.child("user").child(uuid).child("property").child(names).child("location").setValue(adds)
                database.child("user").child(uuid).child("property").child(names).child("phone").setValue(phones.toLong())
                Toast.makeText(applicationContext,"Place added SuccessFully",Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext,ViewAddPlaces::class.java))
            }else{
                Toast.makeText(applicationContext,"Please Fill All The Details",Toast.LENGTH_LONG).show()
            }

        }
        img.setOnClickListener {
            selectImgFromGallery()
        }
    }

    private fun selectImgFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            GALLERY_REQUEST_CODE
        )
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            val file_uri = data.data
            if (file_uri != null) {
                uploadPdfToFirebase(file_uri)
            }
        }
    }
    private fun uploadPdfToFirebase(fileUri: Uri) {
        if (fileUri != null) {


            auth = Firebase.auth
            database = Firebase.database.reference

            val user=auth.currentUser
            val uuid= user?.uid
            val fileName = uuid.toString() +".pdf"
            val refStorage = FirebaseStorage.getInstance().reference.child("pdfs/$uuid/$fileName")
            progress=findViewById(R.id.progressBar2)
            progress.visibility= View.VISIBLE

            refStorage.putFile(fileUri)
                .addOnSuccessListener(

                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
//                            if (uuid != null) {
////                                database.child("user").child(uuid).child("govid").setValue(imageUrl)
//                            }
                            Toast.makeText(applicationContext,imageUrl, Toast.LENGTH_LONG).show()
                            progress.visibility= View.GONE

                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }
}