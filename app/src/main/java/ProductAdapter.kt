package layout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.laboratoriodedispositivosmoviles.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import layout.com.example.laboratoriodedispositivosmoviles.Product
import layout.com.example.laboratoriodedispositivosmoviles.ProductCardClickListener
import kotlin.coroutines.CoroutineContext

class ProductAdapter(var products: ArrayList<Product>, val productCardClickListener: ProductCardClickListener): RecyclerView.Adapter<ProductAdapter.ViewHolder>(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private lateinit var glideRef: RequestManager

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productView: CardView
        var productImage: ImageView
        var productName: TextView
        var productId: TextView
        var productQuantity: TextView
        var productType: TextView
        var productPrice: TextView
        var productDetails: TextView

        init {
            productView = view.findViewById(R.id.productView)
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

        glideRef = Glide.with(view)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val storageRef = Firebase.storage.reference

        storageRef.child(products[position].image).downloadUrl.addOnSuccessListener { uri ->
            glideRef.load(uri).into(viewHolder.productImage)
        }

        viewHolder.productName.text = products[position].name
        viewHolder.productId.text = "Código: ${products[position].id}"
        viewHolder.productQuantity.text = "Cantidad: ${products[position].quantity}"
        viewHolder.productType.text = "Tipo: ${products[position].type}"
        viewHolder.productPrice.text = "$${products[position].price}"
        viewHolder.productDetails.text = "Observaciones: ${products[position].details}"

        viewHolder.productView.setOnClickListener { launch { productCardClickListener.onProductCardClick(products[position].id) }}
    }

    override fun getItemCount() = products.size

}
