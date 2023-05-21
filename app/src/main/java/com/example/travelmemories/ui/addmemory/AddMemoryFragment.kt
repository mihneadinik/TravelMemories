package com.example.travelmemories.ui.addmemory

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.travelmemories.R
import com.example.travelmemories.databinding.FragmentAddMemoryBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMemoryFragment : Fragment() {
    private lateinit var binding: FragmentAddMemoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMemoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        // set up date picker
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

        // set up places autocomplete
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

        return binding.root
    }

    private fun updateTravelTime(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        binding.dateOfTravelInput.text = sdf.format(calendar.time)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // received autocomplete info
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            binding.locationInput.setText(place.address)
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
}