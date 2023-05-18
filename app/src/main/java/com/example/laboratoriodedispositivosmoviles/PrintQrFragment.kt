package com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.OutputStream

class PrintQrFragment : Fragment() {
    companion object {
        const val PRODUCT = "product"
    }

    private lateinit var file: File

    private lateinit var productId: String

    private lateinit var root: ViewGroup

    private lateinit var imageViewCodigoQr: ImageView
    private lateinit var buttonSalir: Button
    private lateinit var buttonDescargar: Button

    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getString(PRODUCT).toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_get_qr, container, false) as ViewGroup

        buttonSalir = root.findViewById(R.id.buttonSalir)
        buttonSalir.setOnClickListener { exit() }

        bitmap = encodeAsBitmap(productId)

        imageViewCodigoQr = root.findViewById(R.id.imageViewCodigoQr)
        imageViewCodigoQr.setImageBitmap(bitmap)

        buttonDescargar = root.findViewById(R.id.buttonDescargar)
        buttonDescargar.setOnClickListener { saveToGallery() }

        return root
    }
    private fun encodeAsBitmap(str: String): Bitmap {
        val result: BitMatrix = MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 400, 400, null)
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result.get(x, y)) -0x1000000 else -0x1
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun exit() {
        val action = PrintQrFragmentDirections.actionPrintQrFragmentToInventoryFragment()
        root.findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToGallery() {
        createFile()
        val content = createContent()
        val uri = save(content)
        clearContents(content, uri)
    }

    private fun createFile() {
        val dir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        file = File.createTempFile("QR_${id}_", ".jpg", dir)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createContent(): ContentValues {
        val fileName = file.name
        val fileType = "image/jpg"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private fun save(content: ContentValues): Uri {
        var outputStream: OutputStream? = null
        var uri: Uri? = null
        activity?.contentResolver.also { resolver ->
            if (resolver != null) {
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
                outputStream = resolver.openOutputStream(uri!!)
            }
        }
        outputStream.use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        }
        return uri!!
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun clearContents(content: ContentValues, uri: Uri) {
        content.clear()
        content.put(MediaStore.MediaColumns.IS_PENDING,0)
        activity?.contentResolver?.update(uri, content, null, null)
    }
}