package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yasarkiremitci.chatgbt.Fragments.Onboardings1Directions
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.databinding.FragmentOnboardings1Binding

class Onboardings1 : Fragment() {
    private lateinit var binding: FragmentOnboardings1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardings1Binding.inflate(inflater, container, false)

        binding.firstNextButton.setOnClickListener {
            val action = Onboardings1Directions.actionOnboardings1ToOnboardings2()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isFirstTime = preferences.getBoolean("isFirstTime", true)

        if (isFirstTime) {
            preferences.edit().putBoolean("isFirstTime", false).apply()
        } else {
            findNavController().navigate(R.id.action_onboardings1_to_onboardings2)
        }
    }
}