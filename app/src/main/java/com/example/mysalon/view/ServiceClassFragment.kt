package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysalon.databinding.FragmentBookInfoBinding
import com.example.mysalon.databinding.FragmentServiceBinding
import com.example.mysalon.databinding.FragmentServicesClassBinding
import com.example.mysalon.view.adapter.ServiceCategoriesAdapter
import com.example.mysalon.viewModel.MainViewModel

class ServiceClassFragment : Fragment() {
    lateinit var binding: FragmentServicesClassBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: ServiceCategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentServicesClassBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getServiceCategories()

        mainViewModel.serviceCategoriesLiveData.observe(requireActivity()){
            adapter = ServiceCategoriesAdapter(this, it)
            binding.rvClass.adapter = adapter
            binding.rvClass.layoutManager = GridLayoutManager(view.context, 2)
        }

    }
}