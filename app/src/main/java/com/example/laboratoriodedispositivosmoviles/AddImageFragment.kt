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
import com.example.laboratoriodedispositivosmoviles.databinding.FragmentAddImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import layout.com.example.laboratoriodedispositivosmoviles.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddImageFragment : Fragment(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        const val PARSED_PRODUCT = "parsedProduct"
    }

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var parsedProduct: String
    private lateinit var requestLabel: String
    private lateinit var imageUri: Uri
    private lateinit var currentPhotoPath: String

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
        binding.buttonAgregar.setOnClickListener { launch { addProduct(parsedProduct) } }
        binding.buttonCamara.setOnClickListener { chooseImageFromCamera() }
        binding.buttonGaleria.setOnClickListener { chooseImageFromGallery() }
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

    private fun goBack() {
        val action = AddImageFragmentDirections.actionAddImageFragmentToAddDataFragment()
        requireView().findNavController().navigate(action)
    }

    private suspend fun addProduct(parsedProduct: String) {
        val productDatabase = ProductDatabase(requireActivity())
        val product: Product = ProductParser.parseProductFromJson(parsedProduct)

        val imageStorage = ImageStorageHandler(requireActivity())
        val pathString = imageStorage.uploadImage(imageUri, product.id)

        product.image = pathString
        productDatabase.setProduct(product.id, product)

        goToPrintQr(product.id)
    }

    private fun goToPrintQr(productId: String) {
        val action = AddImageFragmentDirections.actionAddImageFragmentToPrintQrFragment(productId)
        requireView().findNavController().navigate(action)
    }
}