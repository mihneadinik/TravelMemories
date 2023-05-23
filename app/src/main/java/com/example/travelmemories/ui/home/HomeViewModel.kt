package com.example.travelmemories.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmemories.memories.Memory
import com.example.travelmemories.memories.MemoryDatabase

class HomeViewModel : ViewModel() {
    private val isInitialized: MutableLiveData<Boolean> = MutableLiveData(false)
    val memoriesList: MutableLiveData<List<Memory>> = MutableLiveData(listOf())

    fun addNewMemory(memory: Memory) {
        (memoriesList.value as MutableList<Memory>).add(memory)
    }

    fun initialiseMemoryList(memories: List<Memory>) {
        memoriesList.value = memories
    }

//    fun mockMemoryList() {
//        if (isInitialized.value == true) return
//
//        isInitialized.value = true
//        _memoriesList.value = listOf(
//            Memory("Cetatea Calmatuiului", "Bucuresti"),
//            Memory("Castelul Hunianzilor", "Hunedoara"),
//            Memory("Cetatea Alba Carolina", "Alba Iulia"),
//            Memory("Cetatea Rasnov", "Rasnov"),
//            Memory("Cetatea Sighisoara", "Sighisoara"),
//            Memory("Cetatea Deva", "Deva"),
//            Memory("Cetatea Fagaras", "Fagaras"),
//            Memory("Cetatea Suceava", "Suceava"),
//            Memory("Cetatea Poenari", "Poenari"),
//            Memory("Cetatea Neamt", "Neamt"),
//            Memory("Cetatea Oradea", "Oradea"),
//            Memory("Cetatea Timisoara", "Timisoara"),
//            Memory("Cetatea Targu Mures", "Targu Mures"),
//            Memory("Cetatea Targoviste", "Targoviste"),
//            Memory("Cetatea Sibiu", "Sibiu"),
//            Memory("Cetatea Cluj", "Cluj"),
//            Memory("Cetatea Brasov", "Brasov"),
//            Memory("Cetatea Baia Mare", "Baia Mare"),
//            Memory("Cetatea Bistrita", "Bistrita"))
//    }
}