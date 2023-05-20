package com.example.laboratoriodedispositivosmoviles


data class Product(var id: String, var image: String, var name: String,
              var quantity: Int, var type: String, var price: Double,
              var details: String, var operations: ArrayList<Operation>)