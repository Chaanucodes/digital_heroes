package com.example.digitalheroes.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalheroes.databinding.HeroDataItemBinding
import com.example.digitalheroes.domain.SuperHeroPieces
import com.example.digitalheroes.network.SuperHeroes

class HeroListAdapter(var showImg : Boolean = true) : ListAdapter<SuperHeroPieces, HeroListAdapter.DataViewHolder>(HeroDiffCall) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, showImg)
    }


    class DataViewHolder private constructor(private val binding: HeroDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SuperHeroPieces, showImg: Boolean) {
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
                val binding = HeroDataItemBinding.inflate(layoutInflater, parent, false)
                return DataViewHolder(binding)
            }
        }

    }


    object HeroDiffCall : DiffUtil.ItemCallback<SuperHeroPieces>() {

        override fun areItemsTheSame(oldItem: SuperHeroPieces, newItem: SuperHeroPieces): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SuperHeroPieces, newItem: SuperHeroPieces): Boolean {
            return oldItem.id == newItem.id
        }

    }
}