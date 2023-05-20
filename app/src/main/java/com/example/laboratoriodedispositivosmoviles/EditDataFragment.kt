package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentEditDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import layout.com.example.laboratoriodedispositivosmoviles.Product
import layout.com.example.laboratoriodedispositivosmoviles.ProductDatabase
import kotlin.coroutines.CoroutineContext

private const val PRODUCT_ID = "productId"

class EditDataFragment : Fragment(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentEditDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var productDatabase: ProductDatabase
    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(PRODUCT_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productDatabase = ProductDatabase(requireActivity())

        launch { getProductData() }

        binding.buttonSalir.setOnClickListener { goToProduct() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }

    private suspend fun getProductData() {
        val product = productDatabase.getProduct(productId)
        if (product == null) {
            Toast.makeText(activity, "No se encontró el producto escaneado", Toast.LENGTH_SHORT).show()
            goToProduct()
        } else {
            initFormWithData(product)
        }
    }

    private fun initFormWithData(product: Product) {
        binding.editTextNombre.setText(product.name)
        binding.editTextTipo.setText(product.type)
        binding.editTextPrecio.setText(product.price.toString())
        binding.editTextObservaciones.setText(product.details)

        binding.buttonSiguiente.setOnClickListener { editData(product) }
    }

    private fun editData(product: Product) {
        val nombre = binding.editTextNombre.text.toString()
        val tipo = binding.editTextTipo.text.toString()
        val precio = binding.editTextPrecio.text.toString().toDoubleOrNull()
        val observaciones = binding.editTextObservaciones.text.toString()

        if (nombre.isNotEmpty() && tipo.isNotEmpty() &&
            precio != null && observaciones.isNotEmpty()) {
            val modifiedProduct = Product(productId,
                product.image,
                nombre,
                product.quantity,
                tipo,
                precio,
                observaciones)

            saveChanges(modifiedProduct)
        }
    }

    private fun saveChanges(product: Product) {
        productDatabase.setProduct(productId, product)

        goToProduct()
    }

    private fun goToProduct() {
        val action = EditDataFragmentDirections.actionEditDataFragmentToViewProductFragment(productId)
        requireView().findNavController().navigate(action)
    }
}