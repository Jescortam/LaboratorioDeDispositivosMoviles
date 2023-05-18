package layout.com.example.laboratoriodedispositivosmoviles

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import layout.ProductAdapter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProductDatabase(val activity: FragmentActivity) {
    private var database: DatabaseReference = Firebase.database.getReference("/products")

    fun setChildEventListener(adapter: ProductAdapter) {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val data = dataSnapshot.value as HashMap<*, *>
                val product = ProductParser.parseProductHashMap(dataSnapshot.key!!, data)

                adapter.products += product
                adapter.notifyItemInserted(adapter.products.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var i = 0
                while (i < adapter.products.size) {
                    if (adapter.products[i].id == dataSnapshot.key) {
                        val data = dataSnapshot.value as HashMap<*, *>
                        adapter.products[i] = ProductParser.parseProductHashMap(dataSnapshot.key!!, data)
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

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        database.addChildEventListener(childEventListener)
    }

    suspend fun getProduct(productId: String): Product? = suspendCoroutine { continuation ->
        var data: HashMap<*, *>
        val productRef = database.child(productId)
        productRef.get().addOnSuccessListener {
            data = it.value as HashMap<*, *>
            if (data.isEmpty()) {
                Toast.makeText(activity, "No se encontró el producto escaneado", Toast.LENGTH_SHORT).show()
                continuation.resume(null)
            }

            continuation.resume(ProductParser.parseProductHashMap(productId, data))
        }.addOnFailureListener {
            continuation.resume(null)
        }
    }

    fun saveChanges(productId: String, product: Product) {

        database.child(productId).setValue(product).addOnCompleteListener {
            Toast.makeText(activity, "Producto editado de manera exitosa", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "Error en la edición del producto", Toast.LENGTH_SHORT).show()
        }
    }
}