package org.kiimo.me.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import org.kiimo.me.R

object DialogUtils {

    var dialogIsActive = false

    fun showGeneralErrorMessage(activity: Activity?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(
            activity, activity.getString(R.string.general_error_message),
            activity.getString(R.string.general_error_message), null
        )
    }

    fun showNetworkFailMeessage(activity: Activity?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(
            activity, activity.getString(R.string.general_error_title),
            activity.getString(R.string.general_error_no_internet), null
        )
    }

    fun showErrorMessage(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        if (!dialogIsActive)
            createMessageDialog(activity, activity.getString(R.string.general_error_title), message, null)
    }

    fun showErrorMessage(activity: Activity?, title: String, message: String) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, title, message, null)
    }

    fun showSuccessMessage(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, activity.getString(R.string.general_success_title), message, null)
    }

    fun ShowInfoDialog(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, "", message, null)
    }

    fun showSuccessMessage(activity: Activity?, message: String?, onClicked: () -> Unit) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(
            activity,
            activity.getString(R.string.general_success_title),
            message,
            DialogInterface.OnClickListener { _, _ ->
                run {
                    onClicked()
                    dialogIsActive = false
                }
            })
    }

    fun showDialogWithTitleAndMessage(activity: Activity?, title: String, message: String?, onClicked: () -> Unit) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, title, message, DialogInterface.OnClickListener { _, _ ->
            run {
                onClicked()
                dialogIsActive = false
            }
        })
    }

    fun showInfoWithTitleDialogMessage(activity: Activity?, title: String, message: String?) {
        if (activity == null || activity.isFinishing)
            return

        createMessageDialog(activity, title, message, null)
    }

    fun showChooseDialog(
        activity: Activity?,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        if (activity == null || activity.isFinishing)
            return

        createOptionDialog(activity, title, message, listener,
            DialogInterface.OnClickListener { dialogInterface, _ ->
                run {
                    dialogInterface.dismiss()
                    dialogIsActive = false
                }
            })
    }


    fun showDialogWithTwoButtons(
        activity: Activity?,
        title: String,
        message: String,
        buttonOneListener: DialogInterface.OnClickListener,
        titleButtonOne: String,
        buttonTwoListener: DialogInterface.OnClickListener,
        titleButtonTwo: String
    ) {
        if (activity == null || activity.isFinishing)
            return

        val builder = getAlertBuilder(activity)

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title)
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message)

        builder.setNegativeButton(titleButtonOne, buttonOneListener)
        builder.setPositiveButton(titleButtonTwo, buttonTwoListener)

        val dialog = builder.create()

        dialog.show()

    }

    private fun createMessageDialog(
        context: Context, title: String, message: String?,
        clickListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = getAlertBuilder(context)

        builder.setMessage(message)

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title)

        if (clickListener == null) {
            val dismissListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                run {
                    dialogInterface.dismiss()
                    dialogIsActive = false
                }
            }
            builder.setPositiveButton(context.getString(R.string.general_error_button), dismissListener)
        } else {
            builder.setNeutralButton(context.getString(R.string.general_error_button), clickListener)
        }

        builder.setOnCancelListener {
            dialogIsActive = false
        }
        val dialog = builder.create()

        dialog.show()
        dialogIsActive = true

        return dialog
    }

    private fun createOptionDialog(
        context: Context, title: String, message: String?,
        clickListener: DialogInterface.OnClickListener?,
        dismissListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = getAlertBuilder(context)

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title)
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message)

        builder.setNegativeButton(context.getString(R.string.general_cancel_button), dismissListener)
        builder.setPositiveButton(context.getString(R.string.general_error_button), clickListener)

        val dialog = builder.create()

        dialog.show()

        return dialog
    }

    private fun getAlertBuilder(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
    }
}