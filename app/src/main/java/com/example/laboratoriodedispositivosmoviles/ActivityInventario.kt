package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import layout.ProductAdapter
import layout.com.example.laboratoriodedispositivosmoviles.Product

class ActivityInventario : AppCompatActivity() {
    private lateinit var logoutButton: ImageButton
    private lateinit var agregarButton: ImageButton
    private lateinit var escanearButton: ImageButton
    private lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        adapter = ProductAdapter(arrayOf())

        databaseSetup()

        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener { logout() }

        agregarButton = findViewById(R.id.agregarButton)
        agregarButton.setOnClickListener { agregarConsumible() }

        escanearButton = findViewById(R.id.escanearButton)
        escanearButton.setOnClickListener { escanear() }

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun databaseSetup() {
        database = Firebase.database.getReference("/products")

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:$dataSnapshot")

                val data = dataSnapshot.value as HashMap<*, *>
                val product = parseHashMap(dataSnapshot.key!!, data)

                adapter.products += product
                adapter.notifyItemInserted(adapter.products.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.key)

                var i = 0
                while (i < adapter.products.size) {
                    if (adapter.products[i].id == dataSnapshot.key) {
                        val data = dataSnapshot.value as HashMap<*, *>
                        adapter.products[i] = parseHashMap(dataSnapshot.key!!, data)
                        break
                    }

                    i++
                }

                adapter.notifyItemChanged(i)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(this@ActivityInventario, "Error al cargar los productos.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.addChildEventListener(childEventListener)
    }

    private fun parseHashMap(id: String, data: HashMap<*, *>): Product {
        return Product(
            id,
            data["image"] as String,
            data["name"] as String,
            data["quantity"] as Number,
            data["type"] as String,
            data["price"] as Number,
            data["details"] as String,
        )
    }

    private fun escanear() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Escanea el código QR")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                Toast.makeText(
                    this,
                    "Código escaneado: ${result.contents}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun logout() {
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
    }

    private fun agregarConsumible() {
        val intent = Intent(this, ActivityAgregar::class.java)
        startActivity(intent)
    }
}