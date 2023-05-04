package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import layout.ProductAdapter
import layout.com.example.laboratoriodedispositivosmoviles.Product

class InventoryFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    lateinit var root: ViewGroup

    private lateinit var logoutButton: ImageButton
    private lateinit var agregarButton: ImageButton
    private lateinit var escanearButton: ImageButton
    private lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ProductAdapter(arrayListOf())

        auth = Firebase.auth

        databaseSetup()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_inventory, container, false) as ViewGroup

        logoutButton = root.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener { logout() }

        agregarButton = root.findViewById(R.id.agregarButton)
        agregarButton.setOnClickListener { addProduct() }

        escanearButton = root.findViewById(R.id.escanearButton)
        escanearButton.setOnClickListener { scan() }

        recyclerView = root.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        return root
    }

    private fun databaseSetup() {
        database = Firebase.database.getReference("/products")

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildAdded223:$dataSnapshot")

                val data = dataSnapshot.value as HashMap<*, *>
                val product = parseHashMap(dataSnapshot.key!!, data)

                adapter.products += product
                adapter.notifyItemInserted(adapter.products.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var i = 0
                while (i < adapter.products.size) {
                    if (adapter.products[i].id == dataSnapshot.key) {
                        val data = dataSnapshot.value as HashMap<*, *>
                        adapter.products[i] = parseHashMap(dataSnapshot.key!!, data)
                        adapter.notifyItemChanged(i)
                        break
                    }

                    i++
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                var i = 0
                while (i < adapter.products.size) {
                    if (adapter.products[i].id == dataSnapshot.key) {
                        adapter.products.removeAt(i)
                        adapter.notifyItemRemoved(i)
                        break
                    }

                    i++
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(activity, "Error al cargar los productos.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.addChildEventListener(childEventListener)
    }

    private fun parseHashMap(id: String, data: HashMap<*, *>): Product {
        val price: Double = if (data["price"] is Long) {
            (data["price"] as Long).toDouble()
        } else {
            data["price"] as Double
        }

        return Product(
            id,
            data["image"] as String,
            data["name"] as String,
            (data["quantity"] as Long).toInt(),
            data["type"] as String,
            price,
            data["details"] as String,
        )
    }

    private fun scan() {
        val integrator = IntentIntegrator(activity)
        integrator.setPrompt("Escanea el código QR")
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                Toast.makeText(
                    activity,
                    "Código escaneado: ${result.contents}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun logout() {
        Firebase.auth.signOut()

        goToLogin()
    }

    private fun goToLogin() {
        val action = InventoryFragmentDirections.actionInventoryFragmentToLoginFragment()
        root.findNavController().navigate(action)
    }


    private fun addProduct() {
        val action = InventoryFragmentDirections.actionInventoryFragmentToAddDataFragment()
        root.findNavController().navigate(action)
    }
}