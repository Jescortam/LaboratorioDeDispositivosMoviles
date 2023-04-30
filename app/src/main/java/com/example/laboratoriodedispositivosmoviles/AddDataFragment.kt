package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
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

class AddDataFragment : Fragment() {
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

        database = Firebase.database.reference
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
        buttonSiguiente.setOnClickListener { next() }

        buttonSalir = root.findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { exit() }

        return root
    }
    private fun next() {
        val action = AddDataFragmentDirections.actionAddDataFragmentToAddImageFragment()
        root.findNavController().navigate(action)
    }

    private fun exit() {
        val action = AddDataFragmentDirections.actionAddDataFragmentToInventoryFragment()
        root.findNavController().navigate(action)
    }

}