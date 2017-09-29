package sk.hyll.dominik.exercise11_shoppinglistdb

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast


class NewItem : DialogFragment() {

    interface NewItemListener {
        fun onNewItemCreated(name: String,count:Int, price:Double)
    }

    private var mListener: NewItemListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as NewItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + getString(R.string.listener_implement_error))
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_new_item, null)
        builder.setView(dialogView)
        val name_ = dialogView.findViewById<EditText>(R.id.name)
        val count_ = dialogView.findViewById<EditText>(R.id.count)
        val price_ = dialogView.findViewById<EditText>(R.id.price)


        builder.setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_yes, DialogInterface.OnClickListener { dialog, id ->
                    val name = name_.text.toString()
                    val count = count_.text.toString().toInt()
                    val price = price_.text.toString().toDouble()

                    if (name == "") return@OnClickListener
                    mListener!!.onNewItemCreated(name,count,price)
                })
                .setNegativeButton(R.string.dialog_cancel) { dialog, id -> Toast.makeText(activity, R.string.cancel, Toast.LENGTH_SHORT).show() }


        return builder.create()
    }
}