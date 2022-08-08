package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mysalon.databinding.FragmentRateUsBinding
import com.example.mysalon.viewModel.MainViewModel

class RateUsFragment : Fragment() {
    lateinit var binding: FragmentRateUsBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentRateUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.btnCancel.setOnClickListener {
            val action = RateUsFragmentDirections.backHomeAction()
            binding.root.findNavController().navigate(action)
        }

        binding.btnContinue.setOnClickListener {
            mainViewModel.addReview(
                binding.rbRating.rating.toDouble(),
                binding.etFeedBack.text.toString()
            )
            val action = RateUsFragmentDirections.backHomeAction()
            binding.root.findNavController().navigate(action)
        }
    }
}