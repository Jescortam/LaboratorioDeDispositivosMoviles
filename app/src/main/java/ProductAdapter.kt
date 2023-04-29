package layout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laboratoriodedispositivosmoviles.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import layout.com.example.laboratoriodedispositivosmoviles.Product

class ProductAdapter(var products: Array<Product>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    val image = R.drawable.pluma_bic

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productImage: ImageView
        var productName: TextView
        var productId: TextView
        var productQuantity: TextView
        var productType: TextView
        var productPrice: TextView
        var productDetails: TextView

        init {
            productImage = view.findViewById(R.id.productImageView)
            productName = view.findViewById(R.id.productNameTextView)
            productId = view.findViewById(R.id.productIdTextView)
            productQuantity = view.findViewById(R.id.productQuantityTextView)
            productType = view.findViewById(R.id.productTypeTextView)
            productPrice = view.findViewById(R.id.productPriceTextView)
            productDetails = view.findViewById(R.id.productDetailsTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_layout, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.productImage.setImageResource(image)
        viewHolder.productName.text = products[position].name
        viewHolder.productId.text = "Código: ${products[position].id}"
        viewHolder.productQuantity.text = "Cantidad: ${products[position].quantity}"
        viewHolder.productType.text = "Tipo: ${products[position].type}"
        viewHolder.productPrice.text = "$${products[position].price}"
        viewHolder.productDetails.text = "Observaciones: ${products[position].details}"
    }

    override fun getItemCount() = products.size

}
