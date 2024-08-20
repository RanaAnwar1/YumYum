package com.example.yumyum.ui.secondactivity.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.yumyum.databinding.CardCategoryBinding
import com.example.yumyum.databinding.ViewItemAreaBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var categories:List<Cat> = emptyList()
    inner class CategoryViewHolder(val binding: CardCategoryBinding): RecyclerView.ViewHolder(binding.root)


    fun setList(newList:List<Cat>){
        categories = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CardCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            cardTitle.text = categories[position].title
            Glide.with(root)
                .load(categories[position].Url)
                .into(cardImageView)
        }
    }

}

data class Cat(
    val title:String,
    val Url:String
)