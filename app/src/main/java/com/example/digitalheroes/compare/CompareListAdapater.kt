package com.example.digitalheroes.compare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalheroes.databinding.CompareDataItemBinding
import com.example.digitalheroes.network.SuperHeroes

class CompareListAdapter(var showImg : Boolean = true) : ListAdapter<SuperHeroes, CompareListAdapter.DataViewHolder>(HeroDiffCall) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, showImg)
    }


    class DataViewHolder private constructor(private val binding: CompareDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SuperHeroes, showImg: Boolean) {
            if (!showImg) {
                binding.heroImageView.visibility = View.GONE
                binding.relativesText.visibility = View.GONE
                binding.showInfoOnly = true
            }else{
                binding.showInfoOnly = false
            }
            binding.searchResult = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): DataViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CompareDataItemBinding.inflate(layoutInflater, parent, false)
                return DataViewHolder(binding)
            }
        }

    }


    object HeroDiffCall : DiffUtil.ItemCallback<SuperHeroes>() {

        override fun areItemsTheSame(oldItem: SuperHeroes, newItem: SuperHeroes): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SuperHeroes, newItem: SuperHeroes): Boolean {
            return oldItem.id == newItem.id
        }

    }
}