package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.FragmentProductDetailBinding
import com.example.mysalon.databinding.FragmentReviewDetailBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.viewModel.MainViewModel

class ReviewDetailFragment : Fragment() {
    lateinit var binding: FragmentReviewDetailBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentReviewDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        mainViewModel.reviewLiveData.value?.let{
            binding.tvUserName.text = it.fullName
            binding.tvReviewDate.text = it.creationDate
            binding.tvDetail.text = it.comment
            Glide.with(requireActivity().applicationContext)
                .load(Constants.BASE_IMAGE_URL + it.profilePic)
                .into(binding.ivProfilePic)
        }

    }
}