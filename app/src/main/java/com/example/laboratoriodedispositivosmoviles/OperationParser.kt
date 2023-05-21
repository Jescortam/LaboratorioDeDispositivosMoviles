package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues.TAG
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
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

        fun parseOperationsFromHashMap(operationListData: ArrayList<HashMap<*, *>>): ArrayList<Operation> {
            val operations = arrayListOf<Operation>()

            operationListData.forEach {operationData ->
                Log.d(TAG, operationData.toString())
                operations += parseOperationFromHashMap(operationData["id"].toString(), operationData)
            }

            return operations
        }

        private fun parseDateFromHashMap(data: HashMap<*, *>): Date {
            return Date(data["time"] as Long)
        }
    }
}