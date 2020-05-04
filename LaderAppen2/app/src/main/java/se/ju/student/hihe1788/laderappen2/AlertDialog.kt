package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * An object for handling alert dialogs.
 */
object AlertDialog {

    /**
     * Creates a simple dialog
     * @param context Application context
     * @param title A string that shows up on the title bar
     * @param message A string that contains the message
     */
    fun createSimpleDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

    /**
     * Creates a dialog with an accept button.
     * @param context Application context
     * @param title A string that shows up on the title bar
     * @param message A string that contains the message
     */
    fun acceptBtDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(MainActivity.mAppContext.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                BluetoothHandler.toggleBluetooth()

            })
            .setNegativeButton(MainActivity.mAppContext.getText(R.string.no),null)
            .show()
    }
}