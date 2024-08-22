package com.example.yumyum.ui.secondactivity.mealdetailsscreen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yumyum.ui.secondactivity.mealdetailsscreen.ingredientsfragment.IngredientsFragment
import com.example.yumyum.ui.secondactivity.mealdetailsscreen.instructionsfragment.InstructionsFragment

class MealDetailsPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsFragment()
            1 -> InstructionsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}