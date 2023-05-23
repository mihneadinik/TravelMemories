package com.example.travelmemories.ui.detailedview

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.travelmemories.R
import com.example.travelmemories.databinding.FragmentAddMemoryBinding
import com.example.travelmemories.databinding.FragmentDetailedViewBinding
import com.example.travelmemories.memories.MemoryDatabase
import com.example.travelmemories.ui.addmemory.AddMemoryFragmentArgs
import com.example.travelmemories.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedViewFragment : Fragment() {
    private lateinit var binding: FragmentDetailedViewBinding
    private val args: AddMemoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedViewBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // retrieve current memory
        val memoryDB = MemoryDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            val currMemory = withContext(Dispatchers.IO) {
                memoryDB.memoryDao().getMemory(args.memoryId)
            }
            binding.memory = currMemory
            binding.moodValue.text = Utils.getMoodLevel(currMemory.moodLevel)

            // set notes text
            if (currMemory.memoryNotes == null || currMemory.memoryNotes == "") {
                binding.notesValue.text = resources.getString(R.string.notes_empty_message)
            } else {
                binding.notesValue.text = currMemory.memoryNotes
            }

            // show map
            if (currMemory.placeLatitude != null && currMemory.placeLongitude != null) {
                val mapFragment = MapFragment(currMemory.placeLatitude!!, currMemory.placeLongitude!!)
                childFragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit()
            } else {
                binding.mapContainer.visibility = View.GONE
            }

            // set picture
            if (currMemory.memoryImage != null) {
                binding.memoryImage.setImageURI(currMemory.memoryImage!!.toUri())
            } else {
                binding.memoryImage.setImageResource(R.drawable.sad_face)
            }
        }
    }
}