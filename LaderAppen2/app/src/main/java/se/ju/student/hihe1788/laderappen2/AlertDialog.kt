package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertDialog {

    fun createSimpleDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

    fun acceptBtDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                BluetoothHandler.toggleBluetooth()

            })
            .setNegativeButton("No",null)
            .show()
    }
}