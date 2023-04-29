package com.example.laboratoriodedispositivosmoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import layout.com.example.laboratoriodedispositivosmoviles.Product
import java.util.UUID

class ActivityAgregar : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var editTextNombre: EditText
    private lateinit var editTextCantidad: EditText
    private lateinit var editTextTipo: EditText
    private lateinit var editTextPrecio: EditText
    private lateinit var editTextObservaciones: EditText
    private lateinit var buttonSiguiente: Button
    private lateinit var buttonSalir: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        database = Firebase.database.reference

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextCantidad = findViewById(R.id.editTextCantidad)
        editTextTipo = findViewById(R.id.editTextTipo)
        editTextPrecio = findViewById(R.id.editTextPrecio)
        editTextObservaciones = findViewById(R.id.editTextObservaciones)

        buttonSiguiente = findViewById(R.id.buttonSiguiente)
        buttonSiguiente.setOnClickListener { seguir() }

        buttonSalir = findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { salir() }
    }

    private fun seguir() {
        val nombre = editTextNombre.text
        val cantidad = editTextCantidad.text
        val tipo = editTextTipo.text
        val precio = editTextPrecio.text
        val observaciones = editTextObservaciones.text

        if (nombre.isNotEmpty() && cantidad.isNotEmpty() && tipo.isNotEmpty() &&
            precio.isNotEmpty() && observaciones.isNotEmpty()) {
            val id = UUID.randomUUID().toString()
            val product = Product(id,
                "",
                nombre.toString(),
                cantidad.toString().toInt(),
                tipo.toString(),
                precio.toString().toFloat(),
                observaciones.toString())

            database.child("products").child(id).setValue(product)
        }

        val intent = Intent(this, ActivityAgregar2::class.java)
        startActivity(intent)
    }

    private fun salir() {
        val intent = Intent(this, ActivityInventario::class.java)
        startActivity(intent)
    }
}