package com.example.fragments

// Created by Sanat Dutta on 2/17/2015.

import android.annotation.TargetApi
import android.os.Build
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
/*
* this fragment is using for chat tab option
* */

class Android : Fragment() {

    private val TAG = "fragment_chat"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_chat, container, false)

        Log.i(TAG, "fragment_chat: onCreateView")

        return view
    }
}
