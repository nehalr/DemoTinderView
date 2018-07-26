package com.example.animation

import android.animation.TypeEvaluator
import android.widget.RelativeLayout.LayoutParams

/*
* when card discard or reverse then this animation class is calling
* and this class is overloading the evaluate method of TypeEvaluator class
* */
class RelativeLayoutParamsEvaluator : TypeEvaluator<LayoutParams> {


    override fun evaluate(fraction: Float, start: LayoutParams,
                          end: LayoutParams): LayoutParams {

        val result = LayoutParams(start)
        result.leftMargin += ((end.leftMargin - start.leftMargin) * fraction).toInt()
        result.rightMargin += ((end.rightMargin - start.rightMargin) * fraction).toInt()
        result.topMargin += ((end.topMargin - start.topMargin) * fraction).toInt()
        result.bottomMargin += ((end.bottomMargin - start.bottomMargin) * fraction).toInt()
        return result
    }

}
