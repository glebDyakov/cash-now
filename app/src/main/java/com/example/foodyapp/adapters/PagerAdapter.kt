package cash.now.cshnw.adapters

import android.os.Bundle
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}

