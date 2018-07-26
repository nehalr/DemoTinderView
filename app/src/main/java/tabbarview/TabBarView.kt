package tabbarview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout

import com.example.demotinderview.R


/*
* created by Aradh Pillai on 1.10.15
* this class is inheriting features of LinearLayout
* using this it's creating TabBarView dynamically
*
*
* */

class TabBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = R.attr.actionBarTabBarStyle) : LinearLayout(context, attrs, defStyle) {
    val mPaint: Paint
    //this pager will hold the instance of viewPager
    //and rearrange the pages using the object
    lateinit var pager: ViewPager
    var delegatePageListener: OnPageChangeListener? = null
    // TabView class objects
    private var tabView1: TabView? = null
    private var tabView2: TabView? = null
    private var tabView3: TabView? = null
    private var mStripHeight: Int = 0
    private var mOffset = 0f
    private var child: View? = null

    private var nextChild: View? = null

    init {

        setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT))

        setWillNotDraw(false)

        mPaint = Paint()
        mPaint.setColor(Color.WHITE)

        mPaint.setAntiAlias(false)

        mStripHeight = (STRIP_HEIGHT * getResources().getDisplayMetrics().density + .5f).toInt()
    }

    //use to set StripColor
    fun setStripColor(color: Int) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color)
            invalidate()
        }
    }

    //Use to set the height of strip
    fun setStripHeight(height: Int) {
        if (mStripHeight != height) {
            mStripHeight = height
            invalidate()
        }
    }

    //using to set the tab based on  Tabindex which is need to pass as parameter.
    fun setSelectedTab(tabIndex: Int) {
        var tabIndex = tabIndex
        if (tabIndex < 0) {
            tabIndex = 0
        }
        val childCount = getChildCount()
        if (tabIndex >= childCount) {
            tabIndex = childCount - 1
        }
        if (mSelectedTab != tabIndex) {
            mSelectedTab = tabIndex
            invalidate()
        }
    }

    fun setOffset(position: Int, offset: Float) {
        if (mOffset != offset) {
            mOffset = offset
            invalidate()
        }
    }

    //this method is using to draw the strip
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the strip manually
        child = getChildAt(mSelectedTab)
        val height = getHeight()
        if (child != null) {
            var left = child!!.getLeft().toFloat()
            var right = child!!.getRight().toFloat()
            if (mOffset > 0f && mSelectedTab < tabCount - 1) {
                nextChild = getChildAt(mSelectedTab + 1)
                if (nextChild != null) {
                    val nextTabLeft = nextChild!!.getLeft().toFloat()
                    val nextTabRight = nextChild!!.getRight().toFloat()
                    left = mOffset * nextTabLeft + (1f - mOffset) * left
                    right = mOffset * nextTabRight + (1f - mOffset) * right
                }
            }
            canvas.drawRect(left, (height - mStripHeight).toFloat(), right, height.toFloat(), mPaint)
        }
    }

    //this method is used to set the pager reference to this.pager object and call the notifyDataSetChanged() method
    //
    fun setViewPager(pager: ViewPager) {
        this.pager = pager

        if (pager.getAdapter() == null) {
            throw IllegalStateException("ViewPager does not have adapter instance.")
        }

        notifyDataSetChanged()
    }

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        mSelectedTab = position
        mOffset = positionOffset

        invalidate()

        if (delegatePageListener != null) {
            delegatePageListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        //based on the position setting tab alpha as 1 if it's selected or others set is as 0.5f
        if (position == 0) {
            tabView1!!.setAlpha(1.0f)
            tabView2!!.setAlpha(0.5f)
            tabView3!!.setAlpha(0.5f)

        } else if (position == 1) {
            tabView1!!.setAlpha(0.5f)
            tabView2!!.setAlpha(1.0f)
            tabView3!!.setAlpha(0.5f)

        } else if (position == 2) {
            tabView1!!.setAlpha(0.5f)
            tabView2!!.setAlpha(0.5f)
            tabView3!!.setAlpha(1.0f)

        }
    }

    fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

        }

        if (delegatePageListener != null) {
            delegatePageListener!!.onPageScrollStateChanged(state)
        }
    }

    fun onPageSelected(position: Int) {
        if (delegatePageListener != null) {
            delegatePageListener!!.onPageSelected(position)
        }
    }

    // it will remove all previous views and creating new view for tab
    fun notifyDataSetChanged() {

        var resId: Int

        this.removeAllViews()

        tabCount = pager.getAdapter().getCount()

        for (i in 0 until tabCount) {

            resId = (pager.getAdapter() as IconTabProvider).getPageIconResId(i)

            if (getResources().getConfiguration().orientation == 1)
                addTabViewP(i, pager.getAdapter().getPageTitle(i).toString(), resId)
            else
                addTabViewL(i, pager.getAdapter().getPageTitle(i).toString(), resId)
        }

        //removing swiping animation effect from viewPager


        getViewTreeObserver().addOnGlobalLayoutListener(object : OnGlobalLayoutListener {

            override fun onGlobalLayout() {

                getViewTreeObserver().removeOnGlobalLayoutListener(this)

                mSelectedTab = pager.getCurrentItem()

            }
        })

    }

    //used to set the properties for landscape screen like icon,text and click-event into tab and then add this tab into View using addView(tab) method
    private fun addTabViewL(i: Int, string: String, pageIconResId: Int) {
        // TODO Auto-generated method stub
        val tab = TabView(getContext())
        //		tab.setIcon(pageIconResId);
        tab.setText(string, pageIconResId)
        tab.setOnClickListener(OnClickListener { pager.setCurrentItem(i, false) })

        this.addView(tab)
    }

    private fun addTabViewP(i: Int, string: String, pageIconResId: Int) {
        // TODO Auto-generated method stub

        if (i == 0) {
            tabView1 = TabView(getContext())

            tabView1!!.setIcon(pageIconResId)
            tabView1!!.setOnClickListener(OnClickListener { pager.setCurrentItem(i) })
            CheatSheet.setup(tabView1!!, string)
            
            this.addView(tabView1)
        } else if (i == 1) {
            tabView2 = TabView(getContext())
            tabView2!!.setIcon(pageIconResId)
            tabView2!!.setOnClickListener(OnClickListener { pager.setCurrentItem(i) })
            CheatSheet.setup(tabView2!!, string)
            this.addView(tabView2)
        } else if (i == 2) {
            tabView3 = TabView(getContext())
            tabView3!!.setIcon(pageIconResId)
            tabView3!!.setOnClickListener(OnClickListener { pager.setCurrentItem(i) })
            CheatSheet.setup(tabView3!!, string)
            this.addView(tabView3)
        }


    }

    //used to set the properties for portrait screen like icon and click-event into tabView objects
    // and then add this tab object into View using addView(tabView1) method
    //here we setup the tabs properties

    fun setOnPageChangeListener(listener: OnPageChangeListener) {
        this.delegatePageListener = listener
    }

    interface IconTabProvider {
        fun getPageIconResId(position: Int): Int
    }

    companion object {
        //strip height has defined here
        private val STRIP_HEIGHT = 6
        var mSelectedTab = 0
        var tabCount: Int = 0//hold the number of tabs
        var a: Int = 0
    }
}