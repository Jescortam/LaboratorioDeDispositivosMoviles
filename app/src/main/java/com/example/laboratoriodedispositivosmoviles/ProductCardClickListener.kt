package layout.com.example.laboratoriodedispositivosmoviles

interface ProductCardClickListener {
    suspend fun onProductCardClick(productId: String)
}