package com.example.laboratoriodedispositivosmoviles

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentEditImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import layout.com.example.laboratoriodedispositivosmoviles.ImageStorageHandler
import layout.com.example.laboratoriodedispositivosmoviles.Product
import layout.com.example.laboratoriodedispositivosmoviles.ProductDatabase
import layout.com.example.laboratoriodedispositivosmoviles.ProductParser
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class EditImageFragment : Fragment(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        const val PARSED_PRODUCT = "parsedProduct"
    }

    private var _binding: FragmentEditImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var parsedProduct: String
    private lateinit var product: Product
    private lateinit var imageStorageHandler: ImageStorageHandler
    private lateinit var requestLabel: String
    private lateinit var imageUri: Uri
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parsedProduct = it.getString(PARSED_PRODUCT).toString()
        }

        product = ProductParser.parseProductFromJson(parsedProduct)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAtras.setOnClickListener { goBack() }
        binding.buttonAgregar.setOnClickListener { launch { editProduct() } }
        binding.buttonCamara.setOnClickListener { chooseImageFromCamera() }
        binding.buttonGaleria.setOnClickListener { chooseImageFromGallery() }

        imageStorageHandler = ImageStorageHandler(requireActivity())
        val pathString = ImageStorageHandler.getPathString(product.id)
        val imageView = binding.imageViewCargarImagen
        imageStorageHandler.getImageFromStorageToImageView(pathString, imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    private fun chooseImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile = createImageFile()
        imageUri = FileProvider.getUriForFile(
            Objects.requireNonNull(requireContext()),
            BuildConfig.APPLICATION_ID + ".provider",
            imageFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
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
                if (requestLabel == "GALLERY") {
                    imageUri = result.data!!.data!!
                }

                binding.imageViewCargarImagen.setImageURI(imageUri)
            } else {
                Toast.makeText(activity, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }


    private suspend fun editProduct() {
        val productDatabase = ProductDatabase(requireActivity())
        val product: Product = ProductParser.parseProductFromJson(parsedProduct)

        imageStorageHandler.deleteImage(product.image)
        val pathString = imageStorageHandler.uploadImage(imageUri, product.id)

        product.image = pathString
        productDatabase.setProduct(product.id, product)

        goToInventory()
    }

    private fun goToInventory() {
        val action = EditImageFragmentDirections.actionEditImageFragmentToInventoryFragment()
        requireView().findNavController().navigate(action)
    }

    private fun goBack() {
        val action = EditImageFragmentDirections.actionEditImageFragmentToEditDataFragment(product.id)
        requireView().findNavController().navigate(action)
    }
}