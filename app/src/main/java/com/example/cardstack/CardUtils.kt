package com.example.cardstack

import android.util.Log
import android.view.View
import android.widget.RelativeLayout.LayoutParams


/*
* CardUtils is using for set the card scale , get the selected layout param, scaleFrom, moveFrom and direction of card.
* All these method are static which means it is going to access from some others classes and it returns the value based on the
* passed parameter.
*
* */
object CardUtils {
    internal val DIRECTION_TOP_LEFT = 0
    internal val DIRECTION_TOP_RIGHT = 1
    internal val DIRECTION_BOTTOM_LEFT = 2
    internal val DIRECTION_BOTTOM_RIGHT = 3

    //it remove some pixels from the each sides of given view and then set that layout to view using setLayoutParams method.
    fun scale(v: View, pixel: Int) {
        val params = v.layoutParams as LayoutParams
        params.leftMargin -= pixel
        params.rightMargin -= pixel
        params.topMargin -= pixel
        params.bottomMargin -= pixel
        v.layoutParams = params
    }


    //setting the layout margin dynamically to make it as stack view and returning the layoutParams object.
    fun getMoveParams(v: View, upDown: Int, leftRight: Int): LayoutParams {
        val original = v.layoutParams as LayoutParams
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(original);
        val params = cloneParams(original)
        params.leftMargin += leftRight
        params.rightMargin -= leftRight
        Log.d("moving...", params.leftMargin.toString() + "")

        params.topMargin -= upDown
        params.bottomMargin += upDown
        return params
    }

    //used to move the card based on parameter, from this method again it's calling the getMoveParams(v,upDown,leftRight) methods
    fun move(v: View, upDown: Int, leftRight: Int) {
        val params = getMoveParams(v, upDown, leftRight)
        v.layoutParams = params
    }


    //setting the scale of card dynamically based on the parameters
    fun scaleFrom(v: View, params: LayoutParams?, pixel: Int): LayoutParams {
        var params = params
        Log.d("pixel", "onScroll: " + pixel)
        params = cloneParams(params!!)
        params.leftMargin -= pixel
        params.rightMargin -= pixel
        params.topMargin -= pixel
        params.bottomMargin -= pixel
        Log.d("pixel", "onScroll: " + pixel)
        v.layoutParams = params

        return params
    }


    //setting the card margin based on the values using leftRight and Updown variables
    //this method is calling for secondary view

    fun moveFrom(v: View, params: LayoutParams, leftRight: Int, upDown: Int): LayoutParams {
        var params = params
        params = cloneParams(params)
        params.leftMargin += leftRight
        params.rightMargin -= leftRight
        params.topMargin -= upDown
        params.bottomMargin += upDown
        v.layoutParams = params

        return params
    }

    //a copy method for RelativeLayout.LayoutParams for backward compatibility
    fun cloneParams(params: LayoutParams): LayoutParams {
        val copy = LayoutParams(params.width, params.height)
        copy.leftMargin = params.leftMargin
        copy.topMargin = params.topMargin
        copy.rightMargin = params.rightMargin
        copy.bottomMargin = params.bottomMargin
        val rules = params.rules
        for (i in rules.indices) {
            copy.addRule(i, rules[i])
        }
        copy.marginStart = params.marginStart
        copy.marginEnd = params.marginEnd

        return copy
    }


    //giving the distance in between the cards
    fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        Log.d("Data: ", "" + Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toDouble()))
        return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toDouble()).toFloat()
    }


    // in which direction user dum card, that location this method will return ...
    // we can get to know that whether user has liked or dislike the card...
    //eg:
    // 0 | 1
    //--------
    // 2 | 3
    // dislike value :  even this method return the 0 or 2
    // like value: 1 or 3

    fun direction(x1: Float, y1: Float, x2: Float, y2: Float): Int {
        return if (x2 > x1) {//RIGHT
            if (y2 > y1) {//BOTTOM
                DIRECTION_BOTTOM_RIGHT
            } else {//TOP
                DIRECTION_TOP_RIGHT
            }
        } else {//LEFT
            if (y2 > y1) {//BOTTOM
                DIRECTION_BOTTOM_LEFT
            } else {//TOP
                DIRECTION_TOP_LEFT
            }
        }
    }


}
