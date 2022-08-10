package com.example.mysalon.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentBookSelectBarberBinding
import com.example.mysalon.view.book.adapter.BarberAdapter
import com.example.mysalon.viewModel.MainViewModel

class BookSelectBarberFragment : Fragment() {
    lateinit var binding: FragmentBookSelectBarberBinding
    lateinit var adapter: BarberAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookSelectBarberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (mainViewModel.barbersLiveData.value == null) {
            mainViewModel.setBarbers()
        } else {
            adapter = BarberAdapter(
                requireActivity().applicationContext,
                mainViewModel.barbersLiveData.value!!
            )
            binding.rvBarbers.adapter = adapter
            binding.rvBarbers.layoutManager = LinearLayoutManager(view.context)
        }

        mainViewModel.barbersLiveData.observe(requireActivity()) {
            it?.let {
                if(isAdded){
                    adapter = BarberAdapter(requireActivity().applicationContext, it)
                    binding.rvBarbers.adapter = adapter
                }

                binding.rvBarbers.layoutManager = LinearLayoutManager(view.context)

            }

        }



    }
}