package tabbarview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.TextView

import com.example.demotinderview.R


/*
* created by Aradh Pillai on 1.10.15
* This TabView class using to create tab and setting ImageView and Text into TabView
* and adding ripple animation into View.
*
*
* */


class TabView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.actionBarTabStyle) : LinearLayout(context, attrs, defStyle) {

    private val mImageView: ImageView
    private val mTextView: TextView

    init {

        val outValue = TypedValue()
        context.getTheme().resolveAttribute(android.R.attr.actionBarTabTextStyle, outValue, true)

        val txtstyle = outValue.data

        val pad = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources()
                .getDisplayMetrics()).toInt()


        mImageView = ImageView(context)//dynamically create an object of imageView and setting param into it...
        mImageView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        mImageView.setScaleType(ScaleType.CENTER_INSIDE)

        mTextView = TextView(context) //dynamically create a text view and setting these properties into it...
        mTextView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        mTextView.setGravity(Gravity.CENTER)
        mTextView.setCompoundDrawablePadding(pad)
        mTextView.setTextAppearance(context, txtstyle)


        this.addView(mImageView)// adding the imageview object into view
        this.addView(mTextView)// adding textview object into view
        this.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))//setting param into view

        this.setBackgroundResource(R.drawable.list_selector_white)//setting ripple animation into this View.


    }

    fun setIcon(resId: Int) {
        setIcon(getContext().getResources().getDrawable(resId))
    }

    fun setIcon(icon: Drawable?) {
        if (icon != null) {
            mImageView.setVisibility(View.VISIBLE)
            mImageView.setImageDrawable(icon)
        } else {
            mImageView.setImageResource(View.GONE)
        }
    }

    fun setText(resId: Int, ico: Int) {
        setText(getContext().getString(resId), ico)
    }

    fun setText(text: CharSequence, ico: Int) {
        mTextView.setText(text)
        mTextView.setCompoundDrawablesWithIntrinsicBounds(ico, 0, 0, 0)
    }

}