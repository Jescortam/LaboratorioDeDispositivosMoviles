package layout.com.example.laboratoriodedispositivosmoviles

import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class InventoryMovementHandler(private val activity: FragmentActivity) {
    private val productDatabase = ProductDatabase(activity)

    suspend fun substract(productId: String, units: Int) {
        val product = productDatabase.getProduct(productId)

        if (product == null) {
            Toast.makeText(activity, "No se encontró el producto escaneado", Toast.LENGTH_SHORT).show()
            return
        }

        if (product.quantity < units) {
            Toast.makeText(activity, "No hay unidades suficientes", Toast.LENGTH_SHORT).show()
        } else {
            product.quantity -= units
        }

        productDatabase.saveChanges(productId, product)
    }
}