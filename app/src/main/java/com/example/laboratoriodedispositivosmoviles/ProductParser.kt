package layout.com.example.laboratoriodedispositivosmoviles

class ProductParser {
    companion object {
        fun parseProductHashMap(id: String, data: HashMap<*, *>): Product {
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
    }
}