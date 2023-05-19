package com.example.laboratoriodedispositivosmoviles

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentGetQrBinding
import layout.com.example.laboratoriodedispositivosmoviles.QrCode
import layout.com.example.laboratoriodedispositivosmoviles.QrCode.Companion.saveToGallery

class GetQrFragment : Fragment() {
    companion object {
        const val PRODUCT = "product"
    }

    private var _binding: FragmentGetQrBinding? = null
    private val binding get() = _binding!!

    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getString(PRODUCT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetQrBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewCodigoQr.setImageBitmap(QrCode.encodeAsBitmap(productId))
        binding.buttonDescargar.setOnClickListener { saveToGallery(requireActivity(), productId) }
        binding.buttonSalir.setOnClickListener { exit() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun exit() {
        val action = GetQrFragmentDirections.actionPrintQrFragmentToInventoryFragment()
        requireView().findNavController().navigate(action)
    }




}