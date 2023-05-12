package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import layout.com.example.laboratoriodedispositivosmoviles.Product
import java.io.ByteArrayOutputStream

class EditImageFragment : Fragment() {
    companion object {
        const val PARSED_PRODUCT = "parsedProduct"
    }

    private lateinit var imageUri: Uri
    private lateinit var glideRef: RequestManager
    private lateinit var parsedProduct: String
    private lateinit var product: Product
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var root: ViewGroup
    private lateinit var buttonCamara: Button
    private lateinit var buttonGaleria: Button
    private lateinit var buttonAtras: Button
    private lateinit var buttonAgregar: Button
    private lateinit var imageViewCargarImagen : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parsedProduct = it.getString(PARSED_PRODUCT).toString()
        }

        database = Firebase.database.reference
        product = Gson().fromJson(parsedProduct, object: TypeToken<Product>(){}.type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_edit_image, container, false) as ViewGroup

        glideRef = Glide.with(root)

        val storageRef = Firebase.storage.reference

        buttonAtras = root.findViewById(R.id.buttonAtras)
        buttonAtras.setOnClickListener { goBack() }

        buttonAgregar = root.findViewById(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener { editProduct() }

        imageViewCargarImagen = root.findViewById(R.id.imageViewCargarImagen)
        storageRef.child(product.image).downloadUrl.addOnSuccessListener { uri ->
            glideRef.load(uri).into(imageViewCargarImagen)
        }

        buttonCamara = root.findViewById(R.id.buttonCamara)
        buttonCamara.setOnClickListener { chooseImageFromCamera() }

        buttonGaleria = root.findViewById(R.id.buttonGaleria)
        buttonGaleria.setOnClickListener { chooseImageFromGallery() }

        return root
    }

    private fun chooseImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewCargarImagen.setImageBitmap(imageBitmap)

        } else if (requestCode == 2) {
            imageUri = data?.data!!
            imageViewCargarImagen.setImageURI(imageUri)
        }
    }

    private fun editProduct() {
        storage = Firebase.storage
        val storageRef = storage.reference

        storageRef.child(product.image).delete().addOnSuccessListener {
            val pathString = "images/IMG_${product.id}.jpg"
            val imageRef = storageRef.child(pathString)

            imageViewCargarImagen.isDrawingCacheEnabled = true
            imageViewCargarImagen.buildDrawingCache()
            val bitmap = (imageViewCargarImagen.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.d(TAG, "Failure on image uplaod")
            }.addOnSuccessListener {
                Log.d(TAG, "successful image upload")
            }

            product.image = pathString

            database.child("products").child(product.id).setValue(product).addOnCompleteListener {
                Toast.makeText(activity, "Producto editado de manera exitosa", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Error en la edición del producto", Toast.LENGTH_SHORT).show()
            }

            val action = EditImageFragmentDirections.actionEditImageFragmentToInventoryFragment()
            root.findNavController().navigate(action)
        }.addOnFailureListener {
            Log.d(TAG, "Error on deleting old file")
        }
    }

    private fun goBack() {
        val action = EditImageFragmentDirections.actionEditImageFragmentToEditDataFragment(product.id)
        root.findNavController().navigate(action)
    }
}