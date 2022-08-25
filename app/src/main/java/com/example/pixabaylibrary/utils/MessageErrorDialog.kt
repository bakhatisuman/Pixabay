package com.example.pixabaylibrary.utils

import android.content.Context
import com.example.pixabaylibrary.R

class MessageErrorDialog{

    var message:String=""

    constructor(context: Context, message: String) {
        this.message = message

        var builder = androidx.appcompat.app.AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle(context.getString(R.string.error_message))
                .setMessage(this.message)
                .setCancelable(false)
        builder.setNegativeButton(R.string.ok, null)
        builder.create().show()

    }

}