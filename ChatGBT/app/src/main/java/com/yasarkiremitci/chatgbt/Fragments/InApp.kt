package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.databinding.FragmentInAppBinding


class InApp : Fragment() {
    private lateinit var binding: FragmentInAppBinding
     var isPremium = false // Başlangıçta premium üyelik aktif değil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences =requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        val value = sharedPreferences.getString("premium", "false")
        if (value=="true"){
            val action = InAppDirections.actionInAppToHomeChat()
            findNavController().navigate(action)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInAppBinding.inflate(inflater, container, false)

        val isPremiumArg = arguments?.getBoolean("isPremium") ?: false
        setIsPremium(isPremiumArg)

        binding.weekly2.setOnClickListener {
            binding.weekly2.setBackgroundResource(R.drawable._er_everenkli)
            binding.weekly.setBackgroundResource(R.drawable.cerceve2)
            binding.weekly3.setBackgroundResource(R.drawable.cerceve2)
            binding.tik1.setImageResource(R.drawable.icon_selected)
            binding.tik2.setImageResource(R.drawable.vectoryuvarlak1)
            binding.tik3.setImageResource(R.drawable.vectoryuvarlak1)
            setIsPremium(true)
        }
        binding.weekly.setOnClickListener {
            binding.weekly2.setBackgroundResource(R.drawable.cerceve2)
            binding.weekly.setBackgroundResource(R.drawable._er_everenkli)
            binding.weekly3.setBackgroundResource(R.drawable.cerceve2)
            binding.tik1.setImageResource(R.drawable.vectoryuvarlak1)
            binding.tik3.setImageResource(R.drawable.vectoryuvarlak1)
            binding.tik2.setImageResource(R.drawable.icon_selected1)
            setIsPremium(true)
        }
        binding.weekly3.setOnClickListener {
            binding.weekly2.setBackgroundResource(R.drawable.cerceve2)
            binding.weekly.setBackgroundResource(R.drawable.cerceve2)
            binding.weekly3.setBackgroundResource(R.drawable._er_everenkli)
            binding.tik1.setImageResource(R.drawable.vectoryuvarlak1)
            binding.tik2.setImageResource(R.drawable.vectoryuvarlak1)
            binding.tik3.setImageResource(R.drawable.icon_selected1)
            setIsPremium(true)
        }

        binding.Xbutton.setOnClickListener {
            val action = InAppDirections.actionInAppToHomeChat()
            findNavController().navigate(action)
        }

        binding.tryButton.setOnClickListener {
            if (isPremium) {
                val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("premium", "true")
                editor.apply()
                // Premium üyelik aktifse sınırsız mesajlaşma sayfasına geçiş yap
                Toast.makeText(requireContext(), "Premium Sayfasına Geçiş Yapılıyor...", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    val action = InAppDirections.actionInAppToHomeChat()
                    findNavController().navigate(action)
                }, 3000) // 3 saniye (3000 milisaniye) bekletme süresi
            } else {
                // Premium üyelik aktif değilse kullanıcıya uygun mesajı göster
                when {
                    binding.weekly2.isSelected -> {
                        Toast.makeText(requireContext(), "1 Haftalık Premium Üyesiniz.", Toast.LENGTH_SHORT).show()
                    }
                    binding.weekly.isSelected -> {
                        Toast.makeText(requireContext(), "1 Aylık Premium Üyesiniz.", Toast.LENGTH_SHORT).show()
                    }
                    binding.weekly3.isSelected -> {
                        Toast.makeText(requireContext(), "1 Yıllık Premium Üyesiniz.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Premium Üyelik Almalısınız.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    fun setIsPremium(premium: Boolean) {
        isPremium = premium
    }
}


