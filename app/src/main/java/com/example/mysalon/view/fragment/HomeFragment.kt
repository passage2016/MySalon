package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.FragmentHomeBinding
import com.example.mysalon.viewModel.MainViewModel

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        mainViewModel.getDashboard()

        mainViewModel.dashboardLiveData.observe(requireActivity()){
            binding.tvHomeShopStatusText.text = "Shop ${it.isShopOpened}"
            if(it.isShopOpened == "Now Close"){
                Glide.with(this).load(R.drawable.close).into(binding.ivHomeShopStatusIcon)
            }
        }


        binding.cvHomeBookAppointment.setOnClickListener {
            val action = HomeFragmentDirections.bookAction()
            binding.root.findNavController().navigate(action)
        }

        binding.cvHomeMyAppointment.setOnClickListener {
            val action = HomeFragmentDirections.myAppointmentAction()
            binding.root.findNavController().navigate(action)
        }

        binding.cvHomeExploreMore.setOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }

    }
}