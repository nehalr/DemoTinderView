package com.example.fragments

// Created by Sanat Dutta on 2/17/2015.

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.demotinderview.R


/**
 * Created by Aradh Pillai on 1/10/15.
 */
class BlackBerry : Fragment() {

    private val TAG = "fragment_discover"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_discover, container, false)

        Log.i(TAG, "fragment_discover: onCreateView")

        return view
    }
}
