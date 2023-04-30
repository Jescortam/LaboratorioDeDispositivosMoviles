package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class AddImageFragment : Fragment() {
    private lateinit var root: ViewGroup

    private lateinit var buttonAtras: Button
    private lateinit var buttonAgregar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_image, container, false) as ViewGroup

        buttonAtras = root.findViewById(R.id.buttonAtras)
        buttonAtras.setOnClickListener { goBack() }

        buttonAgregar = root.findViewById(R.id.buttonAgregar)
        buttonAgregar.setOnClickListener { addProduct() }

        return root
    }

    private fun goBack() {
        val action = AddImageFragmentDirections.actionAddImageFragmentToAddDataFragment()
        root.findNavController().navigate(action)
    }

    private fun addProduct() {
        val action = AddImageFragmentDirections.actionAddImageFragmentToPrintQrFragment()
        root.findNavController().navigate(action)
    }
}