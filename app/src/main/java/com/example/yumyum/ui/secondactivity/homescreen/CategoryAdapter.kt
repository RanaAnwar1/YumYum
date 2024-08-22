package com.example.yumyum.ui.secondactivity.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Category
import com.example.yumyum.databinding.CardCategoryBinding

class CategoryAdapter(val onClick:(category:String) -> Unit): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories:List<Category> = emptyList()
    inner class CategoryViewHolder(val binding: CardCategoryBinding): RecyclerView.ViewHolder(binding.root)


    fun setList(newList:List<Category>){
        val difference = CategoryDiffUtil(categories,newList)
        val result = DiffUtil.calculateDiff(difference)
        result.dispatchUpdatesTo(this)
        categories = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CardCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            cardTitle.text = categories[position].strCategory
            Glide.with(root)
                .load(categories[position].strCategoryThumb)
                .into(cardImageView)
            cardFavBt.setOnClickListener {
                // TODO: add to fav database
            }
            mealCard.setOnClickListener {
                onClick(categories[position].strCategory)
            }
        }


    }

}