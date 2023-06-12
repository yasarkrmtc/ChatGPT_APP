package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.databinding.FragmentSplashScreenBinding

class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("pre", "false")
        editor.apply()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        startTimer()
        return binding.root
    }

    private fun startTimer() {
        object : CountDownTimer(4000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((4000 - millisUntilFinished) / 40).toInt()
                binding.progressBar.progress = progress
            }

            override fun onFinish() {
                findNavController().navigate(R.id.action_splashScreen_to_onboardings1)
            }
        }.start()
    }
}

