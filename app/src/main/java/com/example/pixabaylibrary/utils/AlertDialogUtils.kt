package com.example.pixabaylibrary.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.pixabaylibrary.R

class AlertDialogUtils {

    interface AlertDialogListener {
        fun onPositiveButtonClick(dialog: AlertDialog)
        fun onNegativeButtonClick(dialog: AlertDialog)
    }

    companion object {

        fun show(
                context: Context,
                positiveButtonLabel: String?,
                negativeButtonLabel: String?,
                message: String?,
                title: String?,
                listener: AlertDialogListener
        ) {
            var dialog: AlertDialog? = null

            val builder = AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
//                    .setTitle(context.getString(R.string.app_name))
                    .setTitle(title)
                    .setMessage(message)
                .setCancelable(false)
            builder.setPositiveButton(positiveButtonLabel) { _, i ->
                listener.onPositiveButtonClick(dialog!!)
            }
            if (negativeButtonLabel != null) {
                builder.setNegativeButton(negativeButtonLabel) { _, i ->
                    listener.onNegativeButtonClick(dialog!!)
                }
            }
            dialog = builder.create()
            dialog.show()

        }

    }

}