package com.example.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adapter.CardStackAdapter

import com.example.cardstack.CardStack
import com.example.cardstack.DefaultStackEventListener
import com.example.demotinderview.R


/**
 * Created by Aradh Pillai on 2/10/15.
 */

/*
* this fragment class is using to show the list of connection into stackCard  based on users location
* and user will have option to discard and like the connection.
*
*
* */

class Iphone : Fragment() {


   lateinit var cardStack: CardStack
    //this class is using for swipe the AdapterView
    lateinit var mCardAdapter: CardStackAdapter


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_connect, container, false)

        init(view)// to initialize widgets
        doRippleBackground() //start ripple background work..
        return view
    }

    private fun init(view: View) {
        //root ripple background initialization
        //rippleBackground1 = (RippleAnimation) view.findViewById(R.id.content);

        //child ripple background initialization
        // rippleBackground2 = (RippleBackground) view.findViewById(R.id.content2);

        //cardStack initialization
        cardStack = view.findViewById(R.id.frame) as CardStack

        //at begin setting rippleBackground visibility as VISIBLE and setting CardStack visibility as GONE
        // rippleBackground1.setVisibility(View.VISIBLE);
        cardStack.setVisibility(View.VISIBLE)

        //creating adapter
        mCardAdapter = CardStackAdapter(getActivity().getApplicationContext(), 0)
    }

    fun doRippleBackground() {


        //start ripple background animations
        //startAnimation();

        //handler created to handle cardStack as well as timer...
        Handler().postDelayed(object : Runnable {
            override fun run() {

                callCardStack()
            }
        }, 8000)

    }

//    //start the background ripple animation...
//    private fun startAnimation() {
//        //if it's not running
//        if (!rippleBackground1!!.isRippleAnimationRunning()) {
//            rippleBackground1!!.startRippleAnimation()//start root ripple animation
//            // rippleBackground2.startRippleAnimation();//start child ripple animation
//        }
//    }
//
//    //this method will stop background ripple animation. if it's running.
//    private fun stopAnimation() {
//        if (rippleBackground1!!.isRippleAnimationRunning()) {
//            rippleBackground1!!.stopRippleAnimation()
//            // rippleBackground2.stopRippleAnimation();
//        }
//    }

    //cardStack view will set it as visible and load the information into stack.
    fun callCardStack() {

        cardStack.setVisibility(View.VISIBLE)
        // rippleBackground1.setVisibility(View.GONE);

        // stopAnimation();//start the ripple background animation.

        //Setting Resource of CardStack
        cardStack.setContentResource(R.layout.card_stack_item)

        //Adding 30 dummy info for CardStack
        for (i in 0..30)
            mCardAdapter.add("" + i)
        cardStack.setAdapter(mCardAdapter)

        //Setting Listener and passing distance as a parameter ,
        //based on the distance card will discard
        //if dragging card distance would be more than specified distance(100) then card will discard or else card will reverse on same position.
        cardStack.setListener(DefaultStackEventListener(300))

    }
}
