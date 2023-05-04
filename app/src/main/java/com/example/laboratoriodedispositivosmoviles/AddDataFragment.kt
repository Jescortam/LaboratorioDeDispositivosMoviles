package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import layout.com.example.laboratoriodedispositivosmoviles.Product
import java.util.*

class AddDataFragment : Fragment() {

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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_data, container, false) as ViewGroup

        editTextNombre = root.findViewById(R.id.editTextNombre)
        editTextCantidad = root.findViewById(R.id.editTextCantidad)
        editTextTipo = root.findViewById(R.id.editTextTipo)
        editTextPrecio = root.findViewById(R.id.editTextPrecio)
        editTextObservaciones = root.findViewById(R.id.editTextObservaciones)

        buttonSiguiente = root.findViewById(R.id.buttonSiguiente)
        buttonSiguiente.setOnClickListener { addData() }

        buttonSalir = root.findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { exit() }

        return root
    }

    private fun addData() {
        val id = UUID.randomUUID().toString()

        val nombre = editTextNombre.text
        val cantidad = editTextCantidad.text.toString().toIntOrNull()
        val tipo = editTextTipo.text
        val precio = editTextPrecio.text.toString().toDoubleOrNull()
        val observaciones = editTextObservaciones.text

        if (nombre.isNotEmpty() && cantidad != null && tipo.isNotEmpty() &&
            precio != null && observaciones.isNotEmpty()) {
            val product = Product(id,
                "",
                nombre.toString(),
                cantidad,
                tipo.toString(),
                precio,
                observaciones.toString())

            goNext(product)
        }

    }

    private fun goNext(product: Product) {
        Log.d(TAG, "object: " + product.toString())
        val parsedProduct = Gson().toJson(product, object: TypeToken<Product>(){}.type)
        Log.d(TAG, "string: " + parsedProduct)
        val action = AddDataFragmentDirections.actionAddDataFragmentToAddImageFragment(parsedProduct)
        root.findNavController().navigate(action)
    }

    private fun exit() {
        val action = AddDataFragmentDirections.actionAddDataFragmentToInventoryFragment()
        root.findNavController().navigate(action)
    }

}