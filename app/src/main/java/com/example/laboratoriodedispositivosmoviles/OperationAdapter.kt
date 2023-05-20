package com.example.laboratoriodedispositivosmoviles

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class OperationAdapter(var operations: ArrayList<Operation>, val activity: FragmentActivity): RecyclerView.Adapter<OperationAdapter.ViewHolder>() {
//    private lateinit var glideRef: RequestManager

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var operationImage: ImageView
        var operationDate: TextView
        var operationQuantity: TextView
        var operationOperator: TextView

        init {
            operationImage = view.findViewById(R.id.operationImageView)
            operationDate = view.findViewById(R.id.operationDateTextView)
            operationQuantity = view.findViewById(R.id.operationQuantityTextView)
            operationOperator = view.findViewById(R.id.operationOperatorTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.operation_layout, viewGroup, false)

//        glideRef = Glide.with(view)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val storageRef = Firebase.storage.reference

//        storageRef.child(operations[position].image).downloadUrl.addOnSuccessListener { uri ->
//            glideRef.load(uri).into(viewHolder.operationImage)
//        }

        val date = operations[position].date

        // Different timezone
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, -1)
        val dateWithOffset = calendar.time

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm")

        if (viewHolder.operationImage.drawable == null) {
            viewHolder.operationImage.visibility = View.GONE
        }

        val operatorText: String
        val operatorColor: String

        if (operations[position].quantity > 0 ) {
            operatorText = "ENTRADA"
            operatorColor = "#008800"
        } else {
            operatorText = "SALIDA"
            operatorColor = "#880000"
        }

        viewHolder.operationOperator.setTextColor(Color.parseColor("#008800"))

        viewHolder.operationDate.text = simpleDateFormat.format(dateWithOffset)
        viewHolder.operationQuantity.text = abs(operations[position].quantity).toString()
        viewHolder.operationOperator.text = operatorText
        viewHolder.operationOperator.setTextColor(Color.parseColor(operatorColor))
    }

    override fun getItemCount() = operations.size
}
