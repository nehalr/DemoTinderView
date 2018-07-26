package com.example.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.demotinderview.R


/**
 * Created by Aradh Pillaion 07/10/15.
 */

/*
* cardStackAdapter which is going to hold list of information and setting it into CardStack
*
* */
class CardStackAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, 0) {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        //supply the layout for your card

        val v = contentView!!.findViewById(R.id.helloText) as TextView
        v.setText(getItem(position))
        return contentView
    }

}
