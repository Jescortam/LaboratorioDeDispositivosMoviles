package layout.com.example.laboratoriodedispositivosmoviles

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.OutputStream

class QrCode {
    companion object {
        fun encodeAsBitmap(productId: String): Bitmap {
            val result: BitMatrix = MultiFormatWriter().encode(productId,
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

        @RequiresApi(Build.VERSION_CODES.Q)
        fun saveToGallery(activity: FragmentActivity, productId: String) {
            val file = createFile(activity, productId)
            val content = createContent(file)
            val uri = save(activity, content, productId)
            clearContents(activity, content, uri)

            Toast.makeText(activity, "La descarga del código fue exitosa", Toast.LENGTH_SHORT).show()
        }

        private fun createFile(activity: FragmentActivity, productId: String): File {
            val dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile("QR_${productId}_", ".jpg", dir)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun createContent(file: File): ContentValues {
            val fileName = file.name
            val fileType = "image/jpg"
            return ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        private fun save(activity: FragmentActivity, content: ContentValues, productId: String): Uri {
            var outputStream: OutputStream? = null
            var uri: Uri? = null
            activity.contentResolver.also { resolver ->
                if (resolver != null) {
                    uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
                    outputStream = resolver.openOutputStream(uri!!)
                }
            }

            val bitmap = encodeAsBitmap(productId)

            outputStream.use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
            return uri!!
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun clearContents(activity: FragmentActivity, content: ContentValues, uri: Uri) {
            content.clear()
            content.put(MediaStore.MediaColumns.IS_PENDING,0)
            activity.contentResolver?.update(uri, content, null, null)
        }
    }
}