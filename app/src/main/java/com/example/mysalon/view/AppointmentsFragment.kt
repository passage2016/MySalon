package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentAppointmentsBinding
import com.example.mysalon.databinding.FragmentBookSelectBarberBinding
import com.example.mysalon.view.adapter.AppointmentAdapter
import com.example.mysalon.view.book.BookSummaryFragmentDirections
import com.example.mysalon.view.book.adapter.BarberAdapter
import com.example.mysalon.viewModel.MainViewModel

class AppointmentsFragment : Fragment() {
    lateinit var binding: FragmentAppointmentsBinding
    lateinit var adapter: AppointmentAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentAppointmentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getAppointments()

        mainViewModel.appointmentsLiveData.observe(requireActivity()) {
            it?.let {
                adapter = AppointmentAdapter(this, it)
                binding.rvAppointments.adapter = adapter
                binding.rvAppointments.layoutManager = LinearLayoutManager(view.context)

            }

        }





    }
}