package com.linlinlin.viewpage2s.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.linlinlin.viewpage2s.MainActivity
import com.linlinlin.viewpage2s.databinding.FragmentHomeBinding
import com.linlinlin.viewpage2s.fragment.sub.SubFragment


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val titles = arrayOf("热点", "社会", "体育", "科技")
    private var currentPosition = 0
    private var oldPosition = 0;
    private var currentState = 0;
    private val positionOffsetPixelsList = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = titles.size

            override fun createFragment(position: Int) = SubFragment.newInstance(titles[position])

        }
        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                currentPosition = position
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING) {
                    positionOffsetPixelsList.add(positionOffsetPixels)
                    Log.i("positionOffsetPixels", "onPageScrolled: $positionOffsetPixels")
                    Log.i("positionOffsetPixels", "size: ${positionOffsetPixelsList.size}")
                    //手右滑减少，左划增加
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                val mainViewPager2 = (requireActivity() as MainActivity).getMainViewPager2();
                super.onPageScrollStateChanged(state)
                currentState = state
                if (state == ViewPager2.SCROLL_STATE_IDLE) {//静止
                    if (currentPosition == oldPosition) {//到头了
                        when (currentPosition) {
                            //左
                            0 -> {
                                //划不动了 或者取消了
                                if (positionOffsetPixelsList.size > 1 && positionOffsetPixelsList.last() == 0 ||positionOffsetPixelsList.last() - positionOffsetPixelsList[0] < 0) {
                                    return
                                }
                                mainViewPager2.currentItem.takeIf {
                                    it > 0
                                }?.also {
                                    (requireActivity() as MainActivity).switchFragment(it - 1)
                                }
                            }
                            //右
                            mainViewPager2.adapter?.itemCount?.minus(1) -> {

                                mainViewPager2.currentItem.takeIf {
                                    it < mainViewPager2.adapter!!.itemCount - 1
                                }?.also {
                                    (requireActivity() as MainActivity).switchFragment(it + 1)
                                }
                            }
                        }
                    }
                    oldPosition = currentPosition
                    positionOffsetPixelsList.clear()
                }
            }
        })
        TabLayoutMediator(
            binding.tabLayout, binding.viewpager2
        ) { tab: TabLayout.Tab, position: Int ->
              tab.text = titles[position]
        }.attach()
    }

}