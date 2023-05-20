package com.example.laboratoriodedispositivosmoviles

import java.util.*
import kotlin.collections.HashMap

class OperationParser {
    companion object {
        fun parseOperationFromHashMap(id: String, data: HashMap<*, *>): Operation {
            return Operation(
                id,
                data["image"].toString(),
                parseDateFromHashMap(data["date"] as HashMap<*, *>),
                (data["quantity"] as Long).toInt()
            )
        }

        private fun parseDateFromHashMap(data: HashMap<*, *>): Date {
            return Date(data["time"] as Long)
        }

    }
}