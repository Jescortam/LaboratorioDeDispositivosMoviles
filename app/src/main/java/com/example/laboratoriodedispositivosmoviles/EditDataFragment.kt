package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import layout.com.example.laboratoriodedispositivosmoviles.Product
import kotlin.collections.HashMap

private const val PRODUCT = "product"

class EditDataFragment : Fragment() {
    private lateinit var productId: String
    private lateinit var data: HashMap<*, *>
    private lateinit var database: DatabaseReference
    private lateinit var root: ViewGroup
    private lateinit var editTextNombre: EditText
    private lateinit var editTextCantidad: EditText
    private lateinit var editTextTipo: EditText
    private lateinit var editTextPrecio: EditText
    private lateinit var editTextObservaciones: EditText
    private lateinit var buttonSiguiente: Button
    private lateinit var buttonSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(PRODUCT).toString()
        }

        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_edit_data, container, false) as ViewGroup

        getProductData()

        return root
    }

    fun getProductData() {
        database.child("products").child(productId).get().addOnSuccessListener {
            data = it.value as HashMap<*, *>
            initFormWithData(data)
        }.addOnFailureListener {
            Toast.makeText(activity, "Error al buscar el producto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFormWithData(data: HashMap<*, *>) {
        editTextNombre = root.findViewById(R.id.editTextNombre)
        editTextNombre.setText(data["name"].toString())

        editTextCantidad = root.findViewById(R.id.editTextCantidad)
        editTextCantidad.setText(data["quantity"].toString())

        editTextTipo = root.findViewById(R.id.editTextTipo)
        editTextTipo.setText(data["type"].toString())

        editTextPrecio = root.findViewById(R.id.editTextPrecio)
        editTextPrecio.setText(data["price"].toString())

        editTextObservaciones = root.findViewById(R.id.editTextObservaciones)
        editTextObservaciones.setText(data["details"].toString())

        buttonSiguiente = root.findViewById(R.id.buttonSiguiente)
        buttonSiguiente.setOnClickListener { editData() }

        buttonSalir = root.findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { exit() }
    }

    private fun editData() {
        val nombre = editTextNombre.text
        val cantidad = editTextCantidad.text.toString().toIntOrNull()
        val tipo = editTextTipo.text
        val precio = editTextPrecio.text.toString().toDoubleOrNull()
        val observaciones = editTextObservaciones.text

        if (nombre.isNotEmpty() && cantidad != null && tipo.isNotEmpty() &&
            precio != null && observaciones.isNotEmpty()) {
            val product = Product(productId,
                data["image"].toString(),
                nombre.toString(),
                cantidad,
                tipo.toString(),
                precio,
                observaciones.toString())

            goNext(product)
        }
    }

    private fun goNext(product: Product) {
        val parsedProduct = Gson().toJson(product, object: TypeToken<Product>(){}.type)
        val action = EditDataFragmentDirections.actionEditDataFragmentToEditImageFragment(parsedProduct)
        root.findNavController().navigate(action)
    }

    private fun exit() {
        val action = EditDataFragmentDirections.actionEditDataFragmentToInventoryFragment()
        root.findNavController().navigate(action)
    }
}