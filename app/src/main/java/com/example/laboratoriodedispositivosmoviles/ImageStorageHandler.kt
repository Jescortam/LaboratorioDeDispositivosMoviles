package layout.com.example.laboratoriodedispositivosmoviles

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageStorageHandler(var activity: FragmentActivity) {
    private val storage = Firebase.storage.reference

    companion object {
        fun getPathString(productId: String): String {
            return "images/IMG_${productId}.jpg"
        }

        fun getImageDataFromImageView(imageView: ImageView): ByteArray {
            val bitmap = Bitmap.createBitmap(
                imageView.width, imageView.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            imageView.draw(canvas)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return baos.toByteArray()
        }
    }

    fun uploadImage(imageView: ImageView, productId: String): String {
        val pathString = getPathString(productId)
        val imageRef = storage.child(pathString)

        val data = getImageDataFromImageView(imageView)

        imageRef.putBytes(data)

        return pathString
    }

    suspend fun deleteImage(pathString: String) = suspendCoroutine { continuation ->
        storage.child(pathString).delete().addOnCompleteListener {
            continuation.resume(it)
        }
    }

    fun getImageFromStorageToImageView(pathString: String, imageView: ImageView) {
        val glideRef = Glide.with(activity)

        storage.child(pathString).downloadUrl.addOnSuccessListener { uri ->
            glideRef.load(uri).into(imageView)
        }
    }
}