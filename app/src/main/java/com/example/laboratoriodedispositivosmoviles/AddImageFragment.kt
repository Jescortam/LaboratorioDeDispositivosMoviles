package com.example.laboratoriodedispositivosmoviles

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentAddImageBinding
import layout.com.example.laboratoriodedispositivosmoviles.*

class AddImageFragment : Fragment() {
    companion object {
        const val PARSED_PRODUCT = "parsedProduct"
    }

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var parsedProduct: String
    private lateinit var requestLabel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            parsedProduct = it.getString(PARSED_PRODUCT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAtras.setOnClickListener { goBack() }
        binding.buttonAgregar.setOnClickListener { addProduct(parsedProduct) }
        binding.buttonCamara.setOnClickListener { chooseImageFromCamera() }
        binding.buttonGaleria.setOnClickListener { chooseImageFromGallery() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun chooseImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        requestLabel = "CAMERA"
        resultLauncher.launch(intent)
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        requestLabel = "GALLERY"
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                if (requestLabel == "CAMERA") {
                    val imageBitmap = result.data!!.extras?.get("data") as Bitmap
                    binding.imageViewCargarImagen.setImageBitmap(imageBitmap)
                } else if (requestLabel == "GALLERY") {
                    binding.imageViewCargarImagen.setImageURI(result.data!!.data!!)
                }
            } else {
                Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    private fun goBack() {
        val action = AddImageFragmentDirections.actionAddImageFragmentToAddDataFragment()
        requireView().findNavController().navigate(action)
    }

    private fun addProduct(parsedProduct: String) {
        val productDatabase = ProductDatabase(requireActivity())
        val product: Product = ProductParser.parseProductFromJson(parsedProduct)

        val imageStorage = ImageStorageHandler(requireActivity())
        val pathString = imageStorage.uploadImage(binding.imageViewCargarImagen, product.id)

        product.image = pathString
        productDatabase.setProduct(product.id, product)

        goToPrintQr(product.id)
    }

    private fun goToPrintQr(productId: String) {
        val action = AddImageFragmentDirections.actionAddImageFragmentToPrintQrFragment(productId)
        requireView().findNavController().navigate(action)
    }
}