package com.tearas.resizevideo.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialog
import com.tearas.resizevideo.core.DialogClickListener
import com.tearas.resizevideo.model.DialogAction
import com.tearas.resizevideo.model.DialogModel

@SuppressLint("StaticFieldLeak")
object DialogUtils {
    private lateinit var context: Context
    private val dialogs = hashMapOf<DialogAction, DialogModel>()
    fun init(context: Context) {
        this.context = context.applicationContext
        initDialogs()
    }

    private fun initDialogs() {
        dialogs[DialogAction.DELETE] = DialogModel(
            context.getString(R.string.are_you_sure),
            context.getString(R.string.are_you_sure_you_want_to_delete_this_video),
            context.getString(R.string.dismiss),
            context.getString(R.string.delete)
        )
        dialogs[DialogAction.BACK] = DialogModel(
            context.getString(R.string.are_you_sure),
            context.getString(R.string.are_you_sure_you_want_to_cancel_this_job_curent_compressed_video_will_be_removed_too),
            context.getString(R.string.dismiss),
            context.getString(R.string.delete)
        )
        dialogs[DialogAction.REPLACE] = DialogModel(
            context.getString(R.string.are_you_sure),
            context.getString(R.string.the_original_files_will_be_replaced_with_the_new_compressed_ones_are_you_sure_you_want_to_do_that),
            context.getString(R.string.dismiss),
            context.getString(R.string.replace)
        )
    }

    private lateinit var dialogDelete: BaseDialog
    private lateinit var dialogBack: BaseDialog
    private lateinit var dialogReplace: BaseDialog
    fun showDialogDelete(
        activity: FragmentActivity,
        callback: DialogClickListener? = null
    ) {
        if (!this::dialogDelete.isInitialized) {
            dialogDelete = BaseDialog(dialogs[DialogAction.DELETE]!!, callback)
        }
        dialogDelete.show(activity)
    }

    fun showDialogBack(
        activity: FragmentActivity,
        callback: DialogClickListener? = null
    ) {
        if (!this::dialogBack.isInitialized) {
            dialogBack = BaseDialog(dialogs[DialogAction.BACK]!!, callback)
        }
        dialogBack.show(activity)
    }

    fun showDialogReplace(
        activity: FragmentActivity,
        callback: DialogClickListener? = null
    ) {
        if (!this::dialogReplace.isInitialized) {
            dialogReplace = BaseDialog(dialogs[DialogAction.REPLACE]!!, callback)
        }
        dialogReplace.show(activity)
    }

    fun DialogFragment.show(activity: FragmentActivity) =
        this.show(activity.supportFragmentManager, DialogFragment::getDialog.name)

}
