package com.sporyap.sporyap.view.on_boarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.sporyap.sporyap.R
import com.sporyap.sporyap.adapter.view_pager.ViewPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    //private val viewModel: OnBoardingViewModel by viewModels()

    private lateinit var viewPager : ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var dotsIndicator: DotsIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.on_boarding_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewPagerAdapter = ViewPagerAdapter(requireContext())
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.setViewPager(viewPager)
        initListeners()
    }

    private fun initListeners(){

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
            override fun onPageSelected(position: Int) {
              if(position > 2){
                  //Navigation.findNavController(requireView()).navigate(R.id.action_onBoardingFragment_to_welcomeFragment)
              }
            }
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("onPageScrollState", state.toString())
            }
        })
    }

    private fun initView(){
        viewPager = requireView().findViewById(R.id.view_pager_on_boarding)
        dotsIndicator = requireView().findViewById(R.id.dots_indicator)
    }
}