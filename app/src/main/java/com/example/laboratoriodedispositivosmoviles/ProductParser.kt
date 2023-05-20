package com.example.laboratoriodedispositivosmoviles

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

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
                data["image"].toString(),
                data["name"].toString(),
                (data["quantity"] as Long).toInt(),
                data["type"].toString(),
                price,
                data["details"].toString(),
                ArrayList()
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