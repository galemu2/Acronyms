package com.galemu00.acronyms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.galemu00.acronyms.data.model.Lf
import com.galemu00.acronyms.databinding.ItemAcronymBinding

class AcronymsAdapter : RecyclerView.Adapter<AcronymsAdapter.ViewHolder>() {


    private var _binding: ItemAcronymBinding? = null
    private val binding: ItemAcronymBinding
        get() = _binding!!


    private val differCallback = object : DiffUtil.ItemCallback<Lf>() {
        override fun areItemsTheSame(oldItem: Lf, newItem: Lf): Boolean {
            return oldItem.lf == newItem.lf
        }

        override fun areContentsTheSame(oldItem: Lf, newItem: Lf): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemAcronymBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(differ.currentList[position])
    }


    class ViewHolder(val binding: ItemAcronymBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: Lf) {
            binding.acronymDefinition.text = item.lf
        }
    }
}