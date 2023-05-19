package com.example.laboratoriodedispositivosmoviles

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentInventoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import layout.ProductAdapter
import layout.com.example.laboratoriodedispositivosmoviles.InventoryMovementHandler
import layout.com.example.laboratoriodedispositivosmoviles.ProductCardClickListener
import layout.com.example.laboratoriodedispositivosmoviles.ProductDatabase
import kotlin.coroutines.CoroutineContext

class InventoryFragment : Fragment(), ProductCardClickListener, CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var productDatabase: ProductDatabase
    private lateinit var adapter: ProductAdapter
    private lateinit var requestLabel: String
    private lateinit var inventoryMovementHandler: InventoryMovementHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ProductAdapter(arrayListOf(), this)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser == null) {
            goToLogin()
            return
        }

        productDatabase = ProductDatabase(requireActivity())
        productDatabase.setChildEventListener(adapter)

        inventoryMovementHandler = InventoryMovementHandler(requireActivity())

        binding.logoutButton.setOnClickListener { logout() }
        binding.agregarButton.setOnClickListener { addProduct() }
        binding.escanearButton.setOnClickListener { scanAndView() }
        binding.venderButton.setOnClickListener { scanAndSell() }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }

    private fun scanAndView() {
        requestLabel = "VIEW"
        scan()
    }

    private fun scanAndSell() {
        requestLabel = "SELL"
        scan()
    }


    private fun scan() {
        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            setPrompt("Escanea el código QR")
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        }
        resultLauncher.launch(integrator.createScanIntent())
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            launch {
                handleResult(result)
            }
    }

    private suspend fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data = result.data.toString()
            if (requestLabel == "VIEW") {
                goToEditData(data)
            } else if (requestLabel == "SELL") {
                sellProductUnit(data)
            }
        } else {
            Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun sellProductUnit(productId: String) {
        inventoryMovementHandler.subtract(productId, 1)
    }

    override suspend fun onProductCardClick(productId: String) {
        goToEditData(productId)
    }

    private fun logout() {
        Firebase.auth.signOut()
        goToLogin()
    }

    private fun goToLogin() {
        val action = InventoryFragmentDirections.actionInventoryFragmentToLoginFragment()
        requireView().findNavController().navigate(action)
    }

    private fun addProduct() {
        val action = InventoryFragmentDirections.actionInventoryFragmentToAddDataFragment()
        requireView().findNavController().navigate(action)
    }

    private fun goToEditData(productId: String) {
        val action = InventoryFragmentDirections.actionInventoryFragmentToEditDataFragment(productId)
        requireView().findNavController().navigate(action)
    }
}