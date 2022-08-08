package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysalon.databinding.FragmentShowCaseBinding
import com.example.mysalon.view.adapter.ShowCasePhotoAdapter
import com.example.mysalon.viewModel.MainViewModel

class ShowCaseFragment : Fragment() {
    lateinit var binding: FragmentShowCaseBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: ShowCasePhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentShowCaseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        mainViewModel.getPhotosByAlbumL()
        mainViewModel.albumIdLiveData.observe(requireActivity()){
            mainViewModel.getPhotosByAlbumL()
        }


        mainViewModel.albumPhotosLiveData.observe(requireActivity()){
            adapter = ShowCasePhotoAdapter(this, it)
            binding.rvClass.adapter = adapter
            binding.rvClass.layoutManager = GridLayoutManager(view.context, 3)
        }

    }
}