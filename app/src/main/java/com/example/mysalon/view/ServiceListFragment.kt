package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentServiceBinding
import com.example.mysalon.view.adapter.ServiceInfoAdapter
import com.example.mysalon.viewModel.MainViewModel

class ServiceListFragment : Fragment() {
    lateinit var binding: FragmentServiceBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: ServiceInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getServicesByCategory(mainViewModel.serviceCategoryIdLiveData.value!!)
        mainViewModel.serviceCategoryIdLiveData.observe(requireActivity()){
            mainViewModel.getServicesByCategory(it)
        }


        mainViewModel.servicesListLiveData.observe(requireActivity()){
            adapter = ServiceInfoAdapter(this, it)
            binding.rvServices.adapter = adapter
            binding.rvServices.layoutManager = LinearLayoutManager(this.context)
        }

    }
}