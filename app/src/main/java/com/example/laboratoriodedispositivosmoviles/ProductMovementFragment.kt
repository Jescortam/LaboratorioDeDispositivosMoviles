package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentProductMovementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val PRODUCT_ID = "productId"

class ProductMovementFragment : Fragment(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentProductMovementBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OperationAdapter
    private lateinit var productDatabase: ProductDatabase
    private lateinit var operationDatabase: OperationDatabase
    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getString(PRODUCT_ID).toString()
        }

        adapter = OperationAdapter(arrayListOf(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productDatabase = ProductDatabase(requireActivity())

        launch { getProduct() }

        binding.buttonSalir.setOnClickListener { goToProduct() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductMovementBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }

    private suspend fun getProduct() {
        val product = productDatabase.getProduct(productId)
        if (product != null) {
            operationDatabase = OperationDatabase(requireActivity(), product)

            initOperationRecyclerView()
            initOperationForm()
            binding.valueUnidadesActuales.text = product.quantity.toString()
        } else {
            goToProduct()
        }
    }

    private fun initOperationRecyclerView() {
        val recyclerView = binding.recyclerView

        operationDatabase.setChildEventListener(adapter, recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }

    private fun initOperationForm() {
        binding.buttonAgregarUnidades.setOnClickListener { makeOperation(1) }
        binding.buttonRestarUnidades.setOnClickListener { makeOperation(-1) }
    }

    private fun makeOperation(sign: Int) {
        val units = binding.editTextUnidades.text.toString()
        if (units.isNotEmpty()) {
            val parsedUnits = Integer.parseInt(binding.editTextUnidades.text.toString()) * sign
            operationDatabase.makeOperation(parsedUnits)

            val formerUnits = Integer.parseInt(binding.valueUnidadesActuales.text.toString())
            val currentUnits = formerUnits + parsedUnits
            if (currentUnits >= 0) {
                binding.valueUnidadesActuales.text = currentUnits.toString()
            }
        }
    }

    private fun goToProduct() {
        val action = ProductMovementFragmentDirections.actionProductMovementFragmentToViewProductFragment(productId)
        requireView().findNavController().navigate(action)
    }
}