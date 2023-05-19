package layout.com.example.laboratoriodedispositivosmoviles

import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageStorageHandler(var activity: FragmentActivity) {
    private val storage = Firebase.storage.reference

    companion object {
        fun getPathString(productId: String): String {
            return "images/IMG_${productId}.jpg"
        }
    }

    suspend fun uploadImage(imageUri: Uri, productId: String): String = suspendCoroutine {continuation ->
        val pathString = getPathString(productId)
        val imageRef = storage.child(pathString)

        imageRef.putFile(imageUri).addOnCompleteListener {
            continuation.resume(pathString)
        }
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