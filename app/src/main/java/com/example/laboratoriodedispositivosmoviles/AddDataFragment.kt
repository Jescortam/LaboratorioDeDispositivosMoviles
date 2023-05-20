package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentAddDataBinding
import java.util.*
import kotlin.collections.ArrayList

class AddDataFragment : Fragment() {
    private var _binding: FragmentAddDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSiguiente.setOnClickListener { addData() }
        binding.buttonSalir.setOnClickListener { exit() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addData() {
        val id = UUID.randomUUID().toString()

        val nombre = binding.editTextNombre.text.toString()
        val cantidad = binding.editTextCantidad.text.toString().toIntOrNull()
        val tipo = binding.editTextTipo.text.toString()
        val precio = binding.editTextPrecio.text.toString().toDoubleOrNull()
        val observaciones = binding.editTextObservaciones.text.toString()

        if (nombre.isNotEmpty() && cantidad != null && tipo.isNotEmpty() &&
            precio != null && observaciones.isNotEmpty()) {
            val operations: MutableList<Operation> = ArrayList()
            operations.add(Operation(UUID.randomUUID().toString(), "", Date(), cantidad))

            val product = Product(id,
                "",
                nombre,
                cantidad,
                tipo,
                precio,
                observaciones,
                arrayListOf()
            )

            goNext(product)
        }
    }

    private fun goNext(product: Product) {
        val parsedProduct = ProductParser.parseProductToJson(product)
        val action = AddDataFragmentDirections.actionAddDataFragmentToAddImageFragment(parsedProduct)
        requireView().findNavController().navigate(action)
    }

    private fun exit() {
        val action = AddDataFragmentDirections.actionAddDataFragmentToInventoryFragment()
        requireView().findNavController().navigate(action)
    }

}