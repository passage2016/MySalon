package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mysalon.R
import com.example.mysalon.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvHomeBookAppointment.setOnClickListener {
            val action = HomeFragmentDirections.bookAction()
            binding.root.findNavController().navigate(action)
        }

        binding.imbBookAppointment.setOnClickListener {
            val action = HomeFragmentDirections.bookAction()
            binding.root.findNavController().navigate(action)
        }

    }
}