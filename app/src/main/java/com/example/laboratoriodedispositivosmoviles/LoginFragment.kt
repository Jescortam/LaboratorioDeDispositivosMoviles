package com.example.laboratoriodedispositivosmoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener { login() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        try {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            goToInventory()
                        } else {
                            Toast.makeText(activity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch(e: Exception) {
            Toast.makeText(activity, "Surgió un problema, intente de nuevo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToInventory() {
        val action = LoginFragmentDirections.actionLoginFragmentToInventoryFragment()
        requireView().findNavController().navigate(action)
    }
}