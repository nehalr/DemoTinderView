package com.example.cardstack

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams

import com.example.animation.RelativeLayoutParamsEvaluator
import com.example.demotinderview.MainActivity
import com.example.demotinderview.R


import java.util.ArrayList
import java.util.HashMap


/*
*  CardAnimator class is used to give animation on Card eg: dislike and like animation of card
*  dragging and dropping animation .
*  setting view of Card and arrange it in the form of stack.
*  shadow effect
*  curve effect of card
*  stack view
* */

class CardAnimator//passing list of cardStack view to mCardCollection. and jumping to setup method
(var mCardCollection: ArrayList<View>) {
    private var mRotation: Float = 0.toFloat() //dislike rotation value
    private var mLayoutsMap: HashMap<View, LayoutParams>? = null//used to store the list of card's view
    private val mRemoteLayouts = arrayOfNulls<LayoutParams>(4)//creating the four list of stack
    private var baseLayout: LayoutParams? = null
    private var mStackMargin = 21//set the margin of cardstack


    //return the top view of stack.
    private val topView: View
        get() = mCardCollection[mCardCollection.size - 1]


    init {
        setup()

    }


    //calling to setup the each card based on view
    private fun setup() {
        mLayoutsMap = HashMap()

        for (v in mCardCollection) {
            //setup basic card layout
            val params = v.layoutParams as LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.width = LayoutParams.MATCH_PARENT
            params.height = LayoutParams.MATCH_PARENT

            v.layoutParams = params

        }

        baseLayout = mCardCollection[0].layoutParams as LayoutParams
        baseLayout = LayoutParams(baseLayout)

        initLayout()//calling this method to arrange all the card based on StackView

        for (v in mCardCollection) {
            val params = v.layoutParams as LayoutParams
            val paramsCopy = LayoutParams(params)
            mLayoutsMap!!.put(v, paramsCopy)
        }

        setupRemotes()

    }

    /* finding the index of view and creating layoutParams using baseLayout object and those height and width setting to view
    *  using method of v.setLayoutParams
    *  and setting card scale size to give curve shape to card
    *  and moving card toward top of others card, and gives look of stack.
    * */
    fun initLayout() {
        val size = mCardCollection.size
        for (v in mCardCollection) {
            var index = mCardCollection.indexOf(v)
            if (index != 0) {
                index -= 1
            }
            val params = LayoutParams(baseLayout)
            v.layoutParams = params

            //calling scale method of CardUtils class to set the margin and than put layout into v
            CardUtils.scale(v, -(size - index - 1) * 8)

            /*calling move method of CardUtils class which is going to move the card one by on based on the mStackMargin
            * first index would be 0 so it won't move card and the based on the index it will multiply with the given margin
                   * and move card to down.
            */
            CardUtils.move(v, index * mStackMargin, 0)

            v.rotation = 0f//setting the rotation of card as 0

            //here calling 3 different shadow layout for each Card
            //which will give shadow effect dynamically ...
            if (index == 0)
            // setting 0 used to add different layout shadow...
                v.background = MainActivity.context.resources.getDrawable(R.drawable.card_stack_bg_shadow3)
            else if (index == 1)
                v.background = MainActivity.context.resources.getDrawable(R.drawable.card_stack_bg_shadow2)
            else if (index == 2)
                v.background = MainActivity.context.resources.getDrawable(R.drawable.card_stack_bg_shadow)
        }
    }

    //setting the each card margins and putting view into array of mRemoteLayouts
    private fun setupRemotes() {
        val topView = topView
        mRemoteLayouts[0] = CardUtils.getMoveParams(topView, REMOTE_DISTANCE, -REMOTE_DISTANCE)
        mRemoteLayouts[1] = CardUtils.getMoveParams(topView, REMOTE_DISTANCE, REMOTE_DISTANCE)
        mRemoteLayouts[2] = CardUtils.getMoveParams(topView, -REMOTE_DISTANCE, -REMOTE_DISTANCE)
        mRemoteLayouts[3] = CardUtils.getMoveParams(topView, -REMOTE_DISTANCE, REMOTE_DISTANCE)
    }


    private fun moveToBack(child: View) {
        val parent = child.parent as ViewGroup
        if (null != parent) {
            parent.removeView(child)
            parent.addView(child, 0)
        }
    }

    //rearrange the mCardCollection list...
    //
    private fun reorder() {
        val temp = topView
        moveToBack(temp)

        for (i in mCardCollection.size - 1 downTo 1) {

            val current = mCardCollection[i - 1]
            //current replace next
            mCardCollection[i] = current


        }
        mCardCollection[0] = temp

        // temp = getTopView();

    }

    //using to set the discard animation of CardStack into ArrayList<Animator>
    fun discard(direction: Int, al: AnimatorListener?) {
        val `as` = AnimatorSet()
        val aCollection = ArrayList<Animator>()


        val topView = topView
        val topParams = topView.layoutParams as LayoutParams
        val layout = LayoutParams(topParams)
        val discardAnim = ValueAnimator.ofObject(RelativeLayoutParamsEvaluator(), layout, mRemoteLayouts[direction])

        discardAnim.addUpdateListener { value -> topView.layoutParams = value.animatedValue as LayoutParams }

        discardAnim.duration = 100//setting the discard removed animation time in ms.
        aCollection.add(discardAnim)//putting the discardAnimation to animation ArrayLIst

        for (i in mCardCollection.indices) {
            val v = mCardCollection[i]

            if (v === topView) continue
            val nv = mCardCollection[i + 1]
            val layoutParams = v.layoutParams as LayoutParams
            val endLayout = LayoutParams(layoutParams)
            val layoutAnim = ValueAnimator.ofObject(RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap!![nv])
            layoutAnim.duration = 100
            layoutAnim.addUpdateListener { value -> v.layoutParams = value.animatedValue as LayoutParams }
            aCollection.add(layoutAnim)
        }

        `as`.addListener(object : AnimatorListenerAdapter() {


            override fun onAnimationEnd(animation: Animator) {
                reorder()
                al?.onAnimationEnd(animation)
                mLayoutsMap = HashMap()
                for (v in mCardCollection) {
                    val params = v.layoutParams as LayoutParams
                    val paramsCopy = LayoutParams(params)
                    mLayoutsMap!!.put(v, paramsCopy)
                }

            }

        })


        `as`.playTogether(aCollection)
        `as`.start()


    }


    //even card is dragged and not discarded by the user then again this card will come on the top of the stack..
    //this method will give the animation of reverse along with the reverse animation duration
    // time in ms... which is I defined as 250

    fun reverse(e1: MotionEvent, e2: MotionEvent) {
        val topView = topView
        val rotationAnim = ValueAnimator.ofFloat(mRotation, 0f)
        rotationAnim.duration = 250
        rotationAnim.addUpdateListener { v -> topView.rotation = v.animatedValue as Float }

        rotationAnim.start()//start the animation

        for (v in mCardCollection) {
            val layoutParams = v.layoutParams as LayoutParams
            val endLayout = LayoutParams(layoutParams)
            val layoutAnim = ValueAnimator.ofObject(RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap!![v])
            layoutAnim.duration = 250
            layoutAnim.addUpdateListener { value -> v.layoutParams = value.animatedValue as LayoutParams }
            layoutAnim.start()
        }

    }


    //dragging method...it's gives the position of object and does the secondary animation of CardStack
    fun drag(e1: MotionEvent, e2: MotionEvent, distanceX: Float,
             distanceY: Float) {

        val topView = topView

        val rotation_coefficient = 20f//

        val layoutParams = topView.layoutParams as LayoutParams
        val topViewLayouts = mLayoutsMap!![topView]
        val x_diff = (e2.rawX - e1.rawX).toInt()
        val y_diff = (e2.rawY - e1.rawY).toInt()

        if (topViewLayouts != null) {
            layoutParams.leftMargin = topViewLayouts.leftMargin + x_diff
        }
        if (topViewLayouts != null) {
            layoutParams.rightMargin = topViewLayouts.rightMargin - x_diff
        }
        if (topViewLayouts != null) {
            layoutParams.topMargin = topViewLayouts.topMargin + y_diff
        }
        if (topViewLayouts != null) {
            layoutParams.bottomMargin = topViewLayouts.bottomMargin - y_diff
        }

        mRotation = x_diff / rotation_coefficient
        topView.rotation = mRotation
        topView.layoutParams = layoutParams

        //animate secondary views.
        for (v in mCardCollection) {
            val index = mCardCollection.indexOf(v)
            if (v !== topView && index != 0) {
                val l = CardUtils.scaleFrom(v ,mLayoutsMap!![v], (Math.abs(x_diff) * 0.05).toInt())
                CardUtils.moveFrom(v, l, 0, (Math.abs(x_diff) * 0.1).toInt())
            }
        }
    }

    //using to set the margin into cardStack
    fun setStackMargin(margin: Int) {
        mStackMargin = margin//passing the value to mStackMargin
        initLayout() //and then calling this method to set the margin into view.
    }

    companion object {
        private val DEBUG_TAG = "CardAnimator"
        private val REMOTE_DISTANCE = 1000
    }


}
