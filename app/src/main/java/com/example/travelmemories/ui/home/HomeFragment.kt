package com.example.travelmemories.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.travelmemories.databinding.FragmentHomeBinding
import com.example.travelmemories.memories.MemoryAdapter

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        // fetch data
        homeViewModel.mockMemoryList()

        // initialise recycler view
        binding.travelMemoriesRecyclerView.adapter = MemoryAdapter(homeViewModel.memoriesList)
        // add a divider between items
        binding.travelMemoriesRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return binding.root
    }
}