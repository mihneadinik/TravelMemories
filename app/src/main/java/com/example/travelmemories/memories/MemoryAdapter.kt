package com.example.travelmemories.memories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmemories.databinding.TravelMemoryItemBinding
import com.example.travelmemories.ui.home.HomeFragment
import com.example.travelmemories.ui.home.HomeFragmentDirections

class MemoryAdapter(private val memoryList: List<Memory>?) : RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val binding = TravelMemoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return memoryList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        memoryList?.get(position)?.let { holder.bind(it) }
    }

    class MemoryViewHolder(private val binding: TravelMemoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memory: Memory) {
            binding.memoryData = memory

            // set click listeners
            binding.cardViewMemoryItem.setOnClickListener {
                // short click show more info
                val action = HomeFragmentDirections.actionHomeFragmentToDetailedViewFragment()
                findNavController(binding.root).navigate(action)
            }
            binding.cardViewMemoryItem.setOnLongClickListener{
                // long click opens edit dialog
                Toast.makeText(binding.root.context, "Long", Toast.LENGTH_SHORT).show()
                true
            }
            binding.executePendingBindings()
        }
    }
}