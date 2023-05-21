package com.example.laboratoriodedispositivosmoviles

import com.example.laboratoriodedispositivosmoviles.OperationParser.Companion.parseOperationsFromHashMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

class ProductParser {
    companion object {
        fun parseProductFromHashMap(id: String, data: HashMap<*, *>): Product {
            var operations = ArrayList<Operation>()
            if (data["operations"] != null) {
                operations = parseOperationsFromHashMap(data["operations"] as ArrayList<HashMap<*, *>>)
            }

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
                operations
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