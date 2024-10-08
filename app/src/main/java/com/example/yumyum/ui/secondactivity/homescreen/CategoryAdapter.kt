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

    inner class CategoryViewHolder(val binding: CardCategoryBinding): RecyclerView.ViewHolder(binding.root)

     var categories:List<Category> = emptyList()
        set(value) {
            val difference = CategoryDiffUtil(field,value)
            val result = DiffUtil.calculateDiff(difference)
            result.dispatchUpdatesTo(this)
            field = value
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
            mealCard.setOnClickListener {
                onClick(categories[position].strCategory)
            }
        }

    }

}