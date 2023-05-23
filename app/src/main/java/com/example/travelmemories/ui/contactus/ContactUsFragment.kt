package com.example.travelmemories.ui.contactus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.travelmemories.R
import com.example.travelmemories.databinding.FragmentContactUsBinding
import com.example.travelmemories.databinding.FragmentDetailedViewBinding

class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        binding.sendEmailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.feedback_email))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject))
            intent.putExtra(Intent.EXTRA_TEXT, binding.emailTextInput.text.toString())

            if(intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Cannot send email", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}