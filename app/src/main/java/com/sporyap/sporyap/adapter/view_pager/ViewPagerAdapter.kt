package com.sporyap.sporyap.adapter.view_pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.sporyap.sporyap.R

class ViewPagerAdapter(private val context : Context) : PagerAdapter() {

    private lateinit var layoutInflater : LayoutInflater

    private val imageArray = arrayOf(
        R.drawable.football,
        R.drawable.basketball,
        R.drawable.ski,
        R.drawable.ski
    )

    override fun getCount(): Int {
        return imageArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.onboarding_slider, container , false)
        val contentLayout = view.findViewById<RelativeLayout>(R.id.content_layout)
        contentLayout.background = ContextCompat.getDrawable(context , imageArray[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}