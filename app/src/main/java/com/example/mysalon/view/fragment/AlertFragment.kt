package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.FragmentAboutUsBinding
import com.example.mysalon.databinding.FragmentAlertBinding
import com.example.mysalon.databinding.FragmentProductDetailBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.view.adapter.AboutUsAdapter
import com.example.mysalon.view.adapter.AlertAdapter
import com.example.mysalon.view.adapter.ProductAdapter
import com.example.mysalon.viewModel.MainViewModel

class AlertFragment : Fragment() {
    lateinit var binding: FragmentAlertBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentAlertBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getAlert()


        mainViewModel.alertLiveData.observe(requireActivity()){
            adapter = AlertAdapter(this, it)
            binding.rvServices.layoutManager = LinearLayoutManager(this.context)
            binding.rvServices.adapter = adapter
        }

    }
}