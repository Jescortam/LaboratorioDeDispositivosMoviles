package com.example.laboratoriodedispositivosmoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityAgregar3 : AppCompatActivity() {
    private lateinit var buttonAtras: Button
    private lateinit var buttonSalir: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar3)

        buttonAtras = findViewById(R.id.buttonAtras)
        buttonAtras.setOnClickListener { regresar() }

        buttonSalir = findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { salir() }
    }

    private fun regresar() {
        val intent = Intent(this, ActivityAgregar2::class.java)
        startActivity(intent)
    }

    private fun salir() {
        val intent = Intent(this, ActivityInventario::class.java)
        startActivity(intent)
    }
}