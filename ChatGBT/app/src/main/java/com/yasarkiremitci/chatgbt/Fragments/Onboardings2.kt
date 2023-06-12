package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yasarkiremitci.chatgbt.Fragments.Onboardings2Directions
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.databinding.FragmentOnboardings2Binding

class Onboardings2 : Fragment() {
    private lateinit var binding : FragmentOnboardings2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardings2Binding.inflate(inflater, container, false)

        binding.secondNextButton.setOnClickListener {
            val action = Onboardings2Directions.actionOnboardings2ToInApp()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isFirstTimeOnboardings2 = preferences.getBoolean("isFirstTimeOnboardings2", true)

        if (isFirstTimeOnboardings2) {
            preferences.edit().putBoolean("isFirstTimeOnboardings2", false).apply()
        } else {
            findNavController().navigate(R.id.action_onboardings2_to_inApp)
        }
    }
}
