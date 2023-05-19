package layout.com.example.laboratoriodedispositivosmoviles

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductParser {
    companion object {
        fun parseProductFromHashMap(id: String, data: HashMap<*, *>): Product {
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

        fun parseProductFromJson(parsedProduct: String): Product {
            return Gson().fromJson(parsedProduct, object: TypeToken<Product>(){}.type)
        }

        fun parseProductToJson(product: Product): String {
            return Gson().toJson(product, object: TypeToken<Product>(){}.type)
        }
    }
}