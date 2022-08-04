package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.FragmentServiceDetailBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.view.adapter.ServiceInfoAdapter
import com.example.mysalon.viewModel.MainViewModel

class ProductDetailFragment : Fragment() {
    lateinit var binding: FragmentServiceDetailBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentServiceDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        mainViewModel.serviceLiveData.value?.let{
            binding.tvServiceName.text = it.serviceName
            binding.tvDuration.text = it.duration.toString()
            binding.tvCost.text = it.cost.toString()
            binding.tvDescription.text = it.description
            Glide.with(requireActivity().applicationContext)
                .load(Constants.BASE_IMAGE_URL + it.servicePic)
                .into(binding.ivServicePic)
        }

    }
}