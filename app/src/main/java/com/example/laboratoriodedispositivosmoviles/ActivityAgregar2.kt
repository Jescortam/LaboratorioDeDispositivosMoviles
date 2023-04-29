package com.example.laboratoriodedispositivosmoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityAgregar2 : AppCompatActivity() {
    private lateinit var buttonAtras: Button
    private lateinit var buttonAgregar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar2)

        buttonAtras = findViewById(R.id.buttonAtras)
        buttonAtras.setOnClickListener { regresar() }

        buttonAgregar = findViewById(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener { agregar() }
    }

    private fun regresar() {
        val intent = Intent(this, ActivityAgregar::class.java)
        startActivity(intent)
    }

    private fun agregar() {
        val intent = Intent(this, ActivityAgregar3::class.java)
        startActivity(intent)
    }
}