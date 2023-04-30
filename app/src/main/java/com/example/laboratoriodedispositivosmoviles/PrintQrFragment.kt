package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class PrintQrFragment : Fragment() {
    private lateinit var root: ViewGroup

    private lateinit var buttonSalir: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_print_qr, container, false) as ViewGroup

        buttonSalir = root.findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { exit() }

        return root
    }

    private fun exit() {
        val action = PrintQrFragmentDirections.actionPrintQrFragmentToInventoryFragment()
        root.findNavController().navigate(action)
    }
}