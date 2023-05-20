package com.example.laboratoriodedispositivosmoviles

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class OperationDatabase(private val activity: FragmentActivity, val product: Product) {
    private val productDatabase = ProductDatabase(activity)
    private val operationDatabase = Firebase.database.getReference("products/${product.id}/operations")

    fun setChildEventListener(adapter: OperationAdapter, recyclerView: RecyclerView) {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val data = dataSnapshot.value as HashMap<*, *>
                val operation = OperationParser.parseOperationFromHashMap(dataSnapshot.key!!, data)

                adapter.operations = (arrayListOf(operation) + adapter.operations) as ArrayList<Operation>
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var i = 0
                while (i < adapter.operations.size) {
                    if (adapter.operations[i].id == dataSnapshot.key) {
                        val data = dataSnapshot.value as HashMap<*, *>
                        adapter.operations[i] =
                            OperationParser.parseOperationFromHashMap(dataSnapshot.key!!, data)
                        adapter.notifyItemChanged(i)
                        break
                    }

                    i++
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                var i = 0
                while (i < adapter.operations.size) {
                    if (adapter.operations[i].id == dataSnapshot.key) {
                        adapter.operations.removeAt(i)
                        adapter.notifyItemRemoved(i)
                        break
                    }

                    i++
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        operationDatabase.addChildEventListener(childEventListener)
    }

    fun makeOperation(units: Int) {
        if (product.quantity + units < 0) {
            Toast.makeText(activity, "No hay unidades suficientes", Toast.LENGTH_SHORT).show()
        } else if (units == 0) {
            Toast.makeText(activity, "Inserte una unidad valida", Toast.LENGTH_SHORT).show()
        } else {
            val id = UUID.randomUUID().toString()

            product.operations += Operation(id, "", Date(), units)
            product.quantity += units

            productDatabase.setProduct(product.id, product)
        }
    }
}