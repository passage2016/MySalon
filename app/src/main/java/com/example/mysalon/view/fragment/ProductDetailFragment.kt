package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.FragmentProductDetailBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.viewModel.MainViewModel

class ProductDetailFragment : Fragment() {
    lateinit var binding: FragmentProductDetailBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        mainViewModel.productLiveData.value?.let{
            binding.tvProductName.text = it.productName
            binding.tvPrice.text = "price: " + it.price.toString()
            binding.tvDescription.text = it.description
            Glide.with(requireActivity().applicationContext)
                .load(Constants.BASE_IMAGE_URL + it.productPic)
                .into(binding.ivServicePic)
        }

    }
}