package com.example.laboratoriodedispositivosmoviles

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import layout.com.example.laboratoriodedispositivosmoviles.ProductCardClickListener
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

        val recyclerView = binding.recyclerView

        productDatabase = ProductDatabase(requireActivity())
        productDatabase.setChildEventListener(adapter, recyclerView)


        binding.logoutButton.setOnClickListener { logout() }
        binding.agregarButton.setOnClickListener { addProduct() }
        binding.escanearButton.setOnClickListener { scanAndView() }
        binding.venderButton.setOnClickListener { scanAndSell() }

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
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
            val id = result.data!!.getStringExtra("SCAN_RESULT")
            if (id != null) {
                if (!id.contains('.') && !id.contains('#') && !id.contains('$') && !id.contains('[') && !id.contains(']')) {
                    if (requestLabel == "VIEW") {
                        goToProduct(id)
                    } else if (requestLabel == "SELL") {
                        sellProductUnit(id)
                    }
                } else {
                    Toast.makeText(requireActivity(), "No es un código valido", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun sellProductUnit(productId: String) {
        val product = productDatabase.getProduct(productId)
        if (product != null) {
            val inventoryMovementHandler = OperationDatabase(requireActivity(), product)
            inventoryMovementHandler.makeOperation(-1)
        }
    }

    override fun onProductCardClick(productId: String) {
        goToProduct(productId)
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

    private fun goToProduct(productId: String) {
        val action = InventoryFragmentDirections.actionInventoryFragmentToViewProductFragment(productId)
        requireView().findNavController().navigate(action)
    }
}