package com.example.yumyum.ui.secondactivity.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.databinding.ViewItemAreaBinding

class AreaAdapter:RecyclerView.Adapter<AreaAdapter.AreaViewHolder>() {

    private var areas:List<String> = emptyList()
    inner class AreaViewHolder(val binding:ViewItemAreaBinding):RecyclerView.ViewHolder(binding.root)

    fun setList(newlist:List<String>){
        areas = newlist
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val binding = ViewItemAreaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AreaViewHolder(binding)
    }

    override fun getItemCount(): Int  = areas.size

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.binding.apply {
            areaRecyclerBt.text = areas[position]
        }
    }

}