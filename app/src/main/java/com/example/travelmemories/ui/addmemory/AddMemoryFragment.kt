package com.example.travelmemories.ui.addmemory

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.travelmemories.R
import com.example.travelmemories.databinding.FragmentAddMemoryBinding
import com.example.travelmemories.memories.Memory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.Calendar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelmemories.memories.MemoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AddMemoryFragment : Fragment() {
    private lateinit var binding: FragmentAddMemoryBinding
    private var coordinates: LatLng? = null
    private var travelType: String? = null
    private val args: AddMemoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMemoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        // set up date picker
        setDatePicker()

        // set up places autocomplete
        setPlacesAutocomplete()

        // set up spinner
        setSpinner()

        // add labels to slider
        binding.moodSlider.setLabelFormatter { value: Float ->
            return@setLabelFormatter getMoodLevel(value)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // retrieve fields from current memory
        if (args.memoryId != -1) {
            updateMemorySettings()
        } else {
            setSaveMemory()
        }
    }

    private fun updateTravelTime(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        binding.dateOfTravelInput.text = sdf.format(calendar.time)
    }

    private fun updateMemorySettings() {
        // change button text
        binding.addMemoryButton.text = resources.getString(R.string.add_button_text_update)

        // fetch data from db
        val memoryDB = MemoryDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val currMemory = memoryDB.memoryDao().getMemory(args.memoryId)
                binding.placeNameInput.setText(currMemory.placeName)
                binding.locationInput.setText(currMemory.placeLocation)
                binding.dateOfTravelInput.text = currMemory.travelTime
                binding.moodSlider.value = currMemory.moodLevel
                binding.notesInput.setText(currMemory.memoryNotes)
            }
        }
        setUpdateMemory()
    }

    private fun setUpdateMemory() {
        // update memory in database
        binding.addMemoryButton.setOnClickListener {
            // get values from input fields
            val placeName = binding.placeNameInput.text.toString()
            val placeLocation = binding.locationInput.text.toString()
            val travelTime = binding.dateOfTravelInput.text.toString()
            val moodLevel = binding.moodSlider.value
            val memoryNotes = binding.notesInput.text.toString()

            // check if all fields are filled
            if (placeName.isEmpty() || placeName == resources.getString(R.string.place_name_hint) ||
                placeLocation.isEmpty() || placeLocation == resources.getString(R.string.location_hint) ||
                travelTime.isEmpty() || travelTime == resources.getString(R.string.date_of_travel_input)
            ) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // update memory
            val memoryDB = MemoryDatabase.getInstance(requireContext())
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // retrieve memory
                    val currMemory = memoryDB.memoryDao().getMemory(args.memoryId)

                    // update fields
                    currMemory.placeName = placeName
                    currMemory.placeLocation = placeLocation
                    currMemory.travelTime = travelTime
                    currMemory.moodLevel = moodLevel
                    currMemory.memoryNotes = memoryNotes
                    currMemory.placeLongitude = coordinates?.longitude
                    currMemory.placeLatitude = coordinates?.latitude

                    // update memory in db
                    memoryDB.memoryDao().updateMemory(currMemory)
                }
            }

            // navigate back to home fragment
            findNavController(binding.root).navigate(R.id.action_detailed_view_fragment_to_home_fragment)
        }
    }

    private fun setSaveMemory() {
        // save memory to database
        binding.addMemoryButton.setOnClickListener {
            // get values from input fields
            val placeName = binding.placeNameInput.text.toString()
            val placeLocation = binding.locationInput.text.toString()
            val travelTime = binding.dateOfTravelInput.text.toString()
            val moodLevel = binding.moodSlider.value
            val memoryNotes = binding.notesInput.text.toString()

            // check if all fields are filled
            if (placeName.isEmpty() || placeName == resources.getString(R.string.place_name_hint) ||
                placeLocation.isEmpty() || placeLocation == resources.getString(R.string.location_hint) ||
                travelTime.isEmpty() || travelTime == resources.getString(R.string.date_of_travel_input)
            ) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // create memory object
            val memory = Memory(
                placeName,
                placeLocation,
                coordinates?.latitude,
                coordinates?.longitude,
                travelType,
                travelTime,
                moodLevel,
                memoryNotes,
                null
            )

            // save memory to database
            val memoryDB = MemoryDatabase.getInstance(requireContext())
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    memoryDB.memoryDao().insertMemory(memory)
                }
            }

            // navigate back to home fragment
            findNavController(binding.root).navigate(R.id.action_detailed_view_fragment_to_home_fragment)
        }
    }

    private fun setDatePicker() {
        val myCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateTravelTime(myCalendar)
        }

        binding.dateOfTravelInput.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setPlacesAutocomplete() {
        Places.initialize(requireContext(), getString(R.string.api_key))
        binding.locationInput.setOnClickListener {
            // info to be received
            val fieldList = listOf(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )

            // create intent
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                fieldList
            ).build(requireContext())

            // start activity
            startActivityForResult(intent, 100)
        }
    }

    private fun setSpinner() {
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_travel_types,
            android.R.layout.simple_spinner_item
        )
        binding.typeOfTravelSpinner.adapter = spinnerAdapter
        binding.typeOfTravelSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                travelType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // received autocomplete info
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            binding.locationInput.setText(place.address)
            coordinates = place.latLng
        }

        // errors occurred
        if (requestCode == 100 && resultCode != RESULT_OK) {
            val status = Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(requireContext(), "Connection error. Please enter address manually.", Toast.LENGTH_SHORT).show()
            Log.i("AddMemoryFragment", status.statusMessage ?: "No status message")

            // accept user input for location
            binding.locationInput.focusable = View.FOCUSABLE
            binding.locationInput.isFocusableInTouchMode = true
            binding.locationInput.requestFocus()
            binding.locationInput.setOnClickListener(null)
        }
    }

    fun getMoodLevel(value: Float): String {
        if (value <= 0.2f) {
            return "Very unhappy"
        }
        if (value <= 0.4f) {
            return "Unhappy"
        }
        if (value <= 0.6f) {
            return "Neutral"
        }
        if (value <= 0.8f) {
            return "Happy"
        }
        return "Very happy"
    }
}