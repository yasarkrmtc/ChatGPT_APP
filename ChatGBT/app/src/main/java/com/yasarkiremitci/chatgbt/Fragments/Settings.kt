package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.databinding.FragmentHomeChatBinding
import com.yasarkiremitci.chatgbt.databinding.FragmentOnboardings1Binding
import com.yasarkiremitci.chatgbt.databinding.FragmentSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            val action = SettingsDirections.actionSettingsToHomeChat()
            findNavController().navigate(action)
        }

        binding.premiumRectangle.setOnClickListener {
            val action = SettingsDirections.actionSettingsToInApp()
            findNavController().navigate(action)
        }

        val sharedPreferences =requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("premium", "false")

        if (value=="true"){
            binding.premiumConstraint.isVisible=false

        }


        binding.rateUsConstraint.setOnClickListener {
            val appPackageName = "com.yasarkiremitci.chatgbt.Fragments" // Uygulamanın paket adını alın
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
        binding.contactUsConstraint.setOnClickListener {
            val url = "https://www.neonapps.co/contact-us" // Yönlendirmek istediğiniz internet sayfasının URL'si
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.privacyConstraint.setOnClickListener {
            val url = "https://www.neonapps.co/" // Yönlendirmek istediğiniz internet sayfasının URL'si
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.termsConstraint.setOnClickListener {
            val url = "https://www.neonapps.co/" // Yönlendirmek istediğiniz internet sayfasının URL'si
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.restoreConstraint.setOnClickListener {
            val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("premium", "true")
            editor.apply()
            Toast.makeText(requireContext(), "Premium Controling", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                // Ana iş parçacığında çalışacak kod bloğu
                delay(2000)
                findNavController().navigate(R.id.action_settings_to_homeChat)
            }
        }






        return binding.root
    }


}