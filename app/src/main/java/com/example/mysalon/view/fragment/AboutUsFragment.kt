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
import com.example.mysalon.databinding.FragmentProductDetailBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.view.adapter.AboutUsAdapter
import com.example.mysalon.view.adapter.ProductAdapter
import com.example.mysalon.viewModel.MainViewModel

class AboutUsFragment : Fragment() {
    lateinit var binding: FragmentAboutUsBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: AboutUsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getContacts()


        mainViewModel.contactsLiveData.observe(requireActivity()){
            adapter = AboutUsAdapter(this, it)
            binding.rvContacts.layoutManager = LinearLayoutManager(this.context)
            binding.rvContacts.adapter = adapter
        }

    }
}