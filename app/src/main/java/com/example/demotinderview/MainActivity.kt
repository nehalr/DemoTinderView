package com.example.demotinderview

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import com.example.fragments.Android
import com.example.fragments.Iphone
import com.example.fragments.BlackBerry
import com.example.utils.CustomViewPager

import tabbarview.TabBarView

class MainActivity : AppCompatActivity() {
    internal lateinit var toolbar: Toolbar
    private val TAG = "MainScreenActivity"
    private var mTabBarView: TabBarView? = null
    private var mMainScreenPagerAdapter: MainScreenPagerAdapter? = null
    private val PAGE_COUNT = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "MainScreenActivity: onCreate()")
        context = this

        setContentView(R.layout.activity_home)






        //Setting Custom Toolbar
        setToolBar()

        //Setting all 3 Customized Tabs
        setUpCustomTabs()

        //Setting the pagerListener
        setPagerListener()

    }

    //using to set custom design , actions and tabView into toolbar
    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val mLayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //setting TabBarView
        val customTabView = mLayoutInflater.inflate(R.layout.custom_tab_view, null)
        mTabBarView = customTabView.findViewById(R.id.customTabBar) as TabBarView
        mTabBarView!!.setStripHeight(7)

        mTabBarView!!.setStripColor(getResources().getColor(R.color.white))

        //setting the properties of ActionBar

        getSupportActionBar()!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
        getSupportActionBar()!!.setHomeButtonEnabled(false)

        //Setting the Customized Toolbar into toolbar object
        toolbar.addView(customTabView)
    }


    private fun setUpCustomTabs() {

        //setting of ViewPager
        mMainScreenPagerAdapter = MainScreenPagerAdapter(getSupportFragmentManager())
        mViewPager = findViewById(R.id.pager) as CustomViewPager
        mViewPager.setAdapter(mMainScreenPagerAdapter)

        //Setting the CustomizedViewPager into Toolbar for tabOption
        mTabBarView!!.setViewPager(mViewPager)


    }


    private fun setPagerListener() {
        mViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mTabBarView!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                Log.i(TAG, "Page: " + position)
                mTabBarView!!.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                mTabBarView!!.onPageScrollStateChanged(state)
            }
        })
    }


    // class is implemented with IconTabProvider Interface as well as extends with FragmentStateAdapter for ViewPager
    //with different tabIcons
    inner class MainScreenPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm), TabBarView.IconTabProvider {


        //Defining the array for Tab icons..which is going to call dynamically and load it into tabBar of toolbar
        private val tab_icons = intArrayOf(R.drawable.android, R.drawable.apple, R.drawable.blackberry)


        //this method is returning the ref of our fragments
        override fun getItem(pos: Int): Fragment? {
            when (pos) {
                0 -> return Iphone()
                1 -> return BlackBerry()
                2 -> return Android()
                else -> return null
            }
        }

        //returning the number of pages
        override fun getCount(): Int {
            return PAGE_COUNT
        }


        //this is TabBarView.IconTabProvider's method to return the position of icon to load into tabBar of Toolbar
        override fun getPageIconResId(position: Int): Int {
            return tab_icons[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Iphone"
                1 -> return "BlackBerry"
                2 -> return "Android"
            }
            return null
        }
    }

    companion object {

        //this context using in
        lateinit var context: Context
        lateinit var mViewPager: CustomViewPager
    }
}