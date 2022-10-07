package com.sporyap.sporyap.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sporyap.sporyap.R

class MaterialDialogHelper {

    fun showDialog(context: Context, message: String, isCancelable : Boolean){
        MaterialAlertDialogBuilder(context).setMessage(message)
            .setPositiveButton(context.getString(R.string.okay)){ dialogInterface, _->
                dialogInterface.dismiss()
            }
            .setCancelable(isCancelable)
            .show()
    }

    fun showCustomDialog(context: Context, message: String?, type : Int){
        val view = View.inflate(context, R.layout.message_pop_up, null)
        val textViewMessage = view.findViewById<TextView>(R.id.text_view_message)
        val imageViewIcon = view.findViewById<ImageView>(R.id.image_view_icon)
        val buttonOkay = view.findViewById<AppCompatButton>(R.id.button_okay)
        textViewMessage.text = message

        when(type){
            1-> imageViewIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info))
            2-> imageViewIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error))
            3-> imageViewIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_success))
        }
        val dialog = MaterialAlertDialogBuilder(context, R.style.MaterialDialogStyle).setView(view)
            .setCancelable(false)
            .show()

        buttonOkay.setOnClickListener {
            if (dialog.isShowing){
                dialog.dismiss()
            }
        }
    }
}