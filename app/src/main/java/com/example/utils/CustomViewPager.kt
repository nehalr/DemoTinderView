package com.example.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Aradh Pillai on 09/10/15.
 *
 *
 *
 *
 * this class is created to avoid the swipe gesture from ViewPager
 * this is customized ViewPager class to override the specific methods of ViewPager to avoid swipe feature.
 */


class CustomViewPager : ViewPager {

    constructor(context: Context) : super(context) {}


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}


    /* when user will touch viewPager in intercept motion then this method will
    *  invoke and it will return "false" it means it won't work so using this method we can avoid this feather from ViewPager
    *
    */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return false
    }

    /* even user will touch viewPager scope then this method will invoke and it will return false value ...
    *  so using this method we can avoid touch event of ViewPager.
    */

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return false
    }
}