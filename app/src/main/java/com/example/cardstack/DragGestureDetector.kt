package com.example.cardstack

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent


//detect both tap and drag based on the gesture
/*
* in this class DragListener interface is defined which is going to use to return the boolean value based on the scope of CardStack
* whether card is start, dragging , dum, etc.
*
*
*
* */


class DragGestureDetector(context: Context, internal var mListener: DragListener?) {
    internal var mGestrueDetector: GestureDetectorCompat
    private var mStarted = false
    private var mDown = false

    private var mOriginalEvent: MotionEvent? = null

    init {
        mGestrueDetector = GestureDetectorCompat(context, MyGestureListener())
    }

    fun onTouchEvent(event: MotionEvent) {
        mGestrueDetector.onTouchEvent(event)
        val action = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_UP -> {
                Log.d(DEBUG_TAG, "Action was UP " + mStarted)
                if (mStarted) {

                        mListener?.onDragEnd(mOriginalEvent, event)

                    mStarted = false
                }
            }

            MotionEvent.ACTION_DOWN -> {
                Log.d(DEBUG_TAG, "Action was down " + mStarted)


                if (mDown) {
                    mListener!!.onDragEnd(mOriginalEvent, event)
                    mDown = false

                }
            }
            MotionEvent.ACTION_SCROLL -> {
                Log.d(DEBUG_TAG, "Scroll " + mStarted)
                if (mStarted) {
                    mListener!!.onDragEnd(mOriginalEvent, event)

                }
                // break;

                //need to set this, quick tap will not generate drap event, so the
                //originalEvent may be null for case action_up
                //which lead to null pointer
                mOriginalEvent = event
            }
        }
    }

    interface DragListener {
        fun onDragStart(e1: MotionEvent, e2: MotionEvent, distanceX: Float,
                        distanceY: Float): Boolean

        fun onDragContinue(e1: MotionEvent, e2: MotionEvent, distanceX: Float,
                           distanceY: Float): Boolean

        fun onDragEnd(e1: MotionEvent?, e2: MotionEvent): Boolean

        fun onTapUp(): Boolean
    }

    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float,
                              distanceY: Float): Boolean {
            if (mListener == null) {
                Log.d(DEBUG_TAG, "NULL")
                return true
            }
            if (mStarted == false) {
                Log.d(DEBUG_TAG, "onDragStart " + mStarted)
                mListener!!.onDragStart(e1, e2, distanceX, distanceY)
                mStarted = true
            } else {
                Log.d(DEBUG_TAG, "onDragContinue " + mStarted)
                mListener!!.onDragContinue(e1, e2, distanceX, distanceY)
                //mOriginalEvent = e1;
                return true
            }
            mOriginalEvent = e1
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(DEBUG_TAG, "OnSingTapUp" + mStarted)
            return mListener!!.onTapUp()
        }
    }

    companion object {
        var DEBUG_TAG = "DragGestureDetector"
    }


}
