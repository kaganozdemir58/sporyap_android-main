package com.sporyap.sporyap.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sporyap.sporyap.R

class DialogHelper {

    companion object{
        fun showErrorDialog(context: Context, message : String){
            MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setTitle(R.string.error_title)
                .setMessage(message)
                .setPositiveButton(context.resources.getString(R.string.okay)){dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }
                .show()
        }
    }
}