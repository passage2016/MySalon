package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentServiceBinding
import com.example.mysalon.databinding.FragmentWorkinngHourBinding
import com.example.mysalon.utils.PageTool
import com.example.mysalon.utils.PaginationScrollListener
import com.example.mysalon.view.adapter.ProductAdapter
import com.example.mysalon.view.adapter.WorkingHourAdapter
import com.example.mysalon.viewModel.MainViewModel


class WorkingHourFragment : Fragment() {
    lateinit var binding: FragmentWorkinngHourBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: WorkingHourAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentWorkinngHourBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getWorkingHour()
        binding.tvStatus.text = mainViewModel.dashboardLiveData.value!!.isShopOpened

        mainViewModel.workingHourLiveData.observe(requireActivity()) {
            adapter = WorkingHourAdapter(this, it)
            binding.rvServices.adapter = adapter
            binding.rvServices.layoutManager = LinearLayoutManager(this.context)
        }

    }
}