package com.example.cardstack

import java.util.ArrayList

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.database.DataSetObserver


class CardStack : RelativeLayout {
    internal var viewCollection = ArrayList<View>()
    //sync?
    var currIndex = 0
        private set
    //return the size of stack
    var stackSize = 4
        private set
    private var mAdapter: ArrayAdapter<*>? = null
    private var mOnTouchListener: View.OnTouchListener? = null
    //private Queue<View> mIdleStack = new Queue<View>;
    private var mCardAnimator: CardAnimator? = null
    private var mEventListener: CardEventListener = DefaultStackEventListener(10)
    private var mContentResource = 0
    var x1: Float = 0.0f
    var y1: Float = 0.0f
    //method used to descard the top view based on direction...which is going to use by other widget
    //eg: using button we can call this method and we can pass the direction as parameter eg: 0 or 2 to discard the card...
    private val mOb = object : DataSetObserver() {
        override fun onChanged() {
            reset(false)
        }
    }

    //returning a new view even
    private val contentView: View?
        get() {
            var contentView: View? = null
            if (mContentResource != 0) {
                val lf = LayoutInflater.from(getContext())
                contentView = lf.inflate(mContentResource, null)
            }
            return contentView

        }


    //return the current index of card

    //only necessary when I need the attrs from xml, this will be used when inflating layout
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {


        //get attrs assign minVisiableNum
        for (i in 0 until stackSize) {
            addContainerViews()
        }
        setupAnimation()
    }

    constructor(context: Context) : super(context) {}

    fun discardTop(direction: Int) {
        mCardAnimator!!.discard(direction, object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(arg0: Animator) {
                mCardAnimator!!.initLayout()
                currIndex++
                mEventListener.discarded(currIndex, direction)

                //mIndex = mIndex%mAdapter.getCount();
                loadLast()

                viewCollection[0].setOnTouchListener(null)
                viewCollection[viewCollection.size - 1]
                        .setOnTouchListener(mOnTouchListener)
            }
        })
    }

    private fun addContainerViews() {
        val v = FrameLayout(getContext())
        viewCollection.add(v)
        addView(v)
    }

    fun setStackMargin(margin: Int) {
        mCardAnimator!!.setStackMargin(margin)
        mCardAnimator!!.initLayout()
    }

    fun setContentResource(res: Int) {
        mContentResource = res
    }

    //when need to reset the card into stack
    fun reset(resetIndex: Boolean) {
        if (resetIndex) currIndex = 0
        removeAllViews()
        viewCollection.clear()
        for (i in 0 until stackSize) {
            addContainerViews()
        }
        setupAnimation()
        loadData()
    }

    fun setVisibleCardNum(visiableNum: Int) {
        stackSize = visiableNum
        reset(false)
    }


    //setting the card animator and putting GestureDetectorListener and overriding the methods of DragListener interface
    //using this method can get to know the dragged card location
    //see the animation effect on that selected card

    fun setThreshold(t: Int) {
        mEventListener = DefaultStackEventListener(t)
    }

    fun setListener(cel: CardEventListener) {
        mEventListener = cel
    }


    //ArrayList



    private fun setupAnimation() {
        val cardView = viewCollection[viewCollection.size - 1]
        mCardAnimator = CardAnimator(viewCollection) //creating an object of cardAnimator
        mCardAnimator?.initLayout() //initialize the cardAnimator using object

        val dd = DragGestureDetector(this@CardStack.getContext(), object : DragGestureDetector.DragListener {
            override fun onDragEnd(e1: MotionEvent?, e2: MotionEvent): Boolean {
               //To change body of created functions use File | Settings | File Templates.

                if (e1 != null) {
                x1 = e1.getRawX()
         y1 = e1.getRawY()
                }
                    val x2 = e2.getRawX()
                    val y2 = e2.getRawY()
                    Log.d("onDragEndMethod", "dis:s")

                    val distance = CardUtils.distance(x1, y1, x2, y2)
                    val direction = CardUtils.direction(x1, y1, x2, y2)





                Log.d("onDragEndMethod", "dis: $distance  direction: $direction")
                val discard = mEventListener.swipeEnd(direction, distance)
                if (discard) {
                    Log.d("onDragEndMethod", "deleted")
                    mCardAnimator!!.discard(direction, object : AnimatorListenerAdapter() {

                        override fun onAnimationEnd(arg0: Animator) {
                            mCardAnimator!!.initLayout()
                            currIndex++
                            mEventListener.discarded(currIndex, direction)

                            //mIndex = mIndex%mAdapter.getCount();
                            loadLast()

                            viewCollection[0].setOnTouchListener(null)
                            viewCollection[viewCollection.size - 1]
                                    .setOnTouchListener(mOnTouchListener)
                        }

                    })
                } else {
                    Log.d("onDragEndMethod", "reverse")
                    if (e1 != null) {
                        mCardAnimator!!.reverse(e1, e2)
                    }
                }
                return true

            }


            //when drag of card will start this method invoke first
            override fun onDragStart(e1: MotionEvent, e2: MotionEvent,
                            distanceX: Float, distanceY: Float): Boolean {
                mCardAnimator!!.drag(e1, e2, distanceX, distanceY)
                return true
            }

            //user dragging the card continue then this method will invoke
            override fun onDragContinue(e1: MotionEvent, e2: MotionEvent,
                               distanceX: Float, distanceY: Float): Boolean {
                val x1 = e1.getRawX()
                val y1 = e1.getRawY()
                val x2 = e2.getRawX()
                val y2 = e2.getRawY()
                //float distance = CardUtils.distance(x1,y1,x2,y2);
                val direction = CardUtils.direction(x1, y1, x2, y2)
                mCardAnimator!!.drag(e1, e2, distanceX, distanceY)
                mEventListener.swipeContinue(direction, Math.abs(x2 - x1), Math.abs(y2 - y1))
                return true
            }

            /*when user dum the card then this method will invoke
            * here it will check the distance between the direction and the distance of the card
            *and based on that this method discard or reverse the card
            */
//            override fun onDragEnd(e1: MotionEvent, e2: MotionEvent): Boolean {
//                //reverse(e1,e2);
//                val x1 = e1.getRawX()
//                val y1 = e1.getRawY()
//                val x2 = e2.getRawX()
//                val y2 = e2.getRawY()
//                Log.d("onDragEndMethod", "dis:s")
//                val distance = CardUtils.distance(x1, y1, x2, y2)
//                val direction = CardUtils.direction(x1, y1, x2, y2)
//                Log.d("onDragEndMethod", "dis: $distance  direction: $direction")
//                val discard = mEventListener.swipeEnd(direction, distance)
//                if (discard) {
//                    Log.d("onDragEndMethod", "deleted")
//                    mCardAnimator!!.discard(direction, object : AnimatorListenerAdapter() {
//
//                        override fun onAnimationEnd(arg0: Animator) {
//                            mCardAnimator!!.initLayout()
//                            currIndex++
//                            mEventListener.discarded(currIndex, direction)
//
//                            //mIndex = mIndex%mAdapter.getCount();
//                            loadLast()
//
//                            viewCollection[0].setOnTouchListener(null)
//                            viewCollection[viewCollection.size - 1]
//                                    .setOnTouchListener(mOnTouchListener)
//                        }
//
//                    })
//                } else {
//                    Log.d("onDragEndMethod", "reverse")
//                    mCardAnimator!!.reverse(e1, e2)
//                }
//                return true
//            }

            override fun onTapUp(): Boolean {
                mEventListener.topCardTapped()
                return true
            }
        }
        )

        mOnTouchListener = object : View.OnTouchListener {
            private val DEBUG_TAG = "MotionEvents"

            override fun onTouch(arg0: View, event: MotionEvent): Boolean {
                dd.onTouchEvent(event)
                return true
            }
        }
        cardView.setOnTouchListener(mOnTouchListener)
    }

    //used to set Adapter
    fun setAdapter(adapter: ArrayAdapter<*>) {
        if (mAdapter != null) {
            mAdapter!!.unregisterDataSetObserver(mOb)
        }
        mAdapter = adapter
        adapter.registerDataSetObserver(mOb)

        loadData()
    }

    //call to load data based on index
    //this method will put Gone or visible property to view...
    private fun loadData() {
        for (i in stackSize - 1 downTo 0) {
            val parent = viewCollection[i] as ViewGroup
            val index = currIndex + stackSize - 1 - i
            if (index > mAdapter!!.getCount() - 1) {
                parent.setVisibility(View.GONE)
            } else {
                val child = mAdapter!!.getView(index, contentView, this)
                parent.addView(child)
                parent.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun loadLast() {
        val parent = viewCollection[0] as ViewGroup

        val lastIndex = stackSize - 1 + currIndex
        if (lastIndex > mAdapter!!.getCount() - 1) {
            parent.setVisibility(View.GONE) //hiding the top view and returning
            return
        }

        val child = mAdapter!!.getView(lastIndex, contentView, parent)
        parent.removeAllViews()// remove all previous view
        parent.addView(child) // and then adding new
    }

    interface CardEventListener {
        //section
        // 0 | 1
        //--------
        // 2 | 3
        // swipe distance, most likely be used with height and width of a view ;

        fun swipeEnd(section: Int, distance: Float): Boolean

        fun swipeStart(section: Int, distance: Float): Boolean

        fun swipeContinue(section: Int, distanceX: Float, distanceY: Float): Boolean

        fun discarded(mIndex: Int, direction: Int)

        fun topCardTapped()
    }
}
