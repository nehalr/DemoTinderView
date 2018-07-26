package com.example.cardstack


/*this is using to put the listener event into cardStack

 */
class DefaultStackEventListener(i: Int) : CardStack.CardEventListener {

    private var mThreshold = 0f

    init {
        mThreshold = i.toFloat()
    }

    override fun swipeEnd(section: Int, distance: Float): Boolean {
        return distance > mThreshold
    }

    override fun swipeStart(section: Int, distance: Float): Boolean {

        return true
    }

    override fun swipeContinue(section: Int, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun discarded(mIndex: Int, direction: Int) {

    }

    override fun topCardTapped() {

    }


}
