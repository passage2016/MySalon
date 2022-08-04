package com.example.mysalon.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentBookSelectServiceBinding
import com.example.mysalon.view.book.adapter.SelectServiceClassAdapter
import com.example.mysalon.viewModel.MainViewModel

class BookSelectServiceFragment : Fragment() {
    lateinit var binding: FragmentBookSelectServiceBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: SelectServiceClassAdapter
    private val args: BookSelectServiceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookSelectServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barberId = args.barberId
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (barberId != -1) {
            mainViewModel.loadServicesByBarber(barberId)

        } else {
            adapter = SelectServiceClassAdapter(
                this,
                mainViewModel.barberServicesTypeLiveData.value!!
            )
            binding.rvServices.adapter = adapter
            binding.rvServices.layoutManager = LinearLayoutManager(view.context)
        }

        mainViewModel.barberServicesTypeLiveData.observe(requireActivity()) {
            it?.let {
                adapter = SelectServiceClassAdapter(this, it)
                binding.rvServices.adapter = adapter
                binding.rvServices.layoutManager = LinearLayoutManager(view.context)
            }
        }

        binding.btnContinue.setOnClickListener {
            if(mainViewModel.barberServicesSelectLiveData.value!!.size == 0){
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Services Error")
                    .setMessage("Please select at least 1 services.")
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            } else {
                mainViewModel.updateAppointmentsSlot()
                val action = BookSelectServiceFragmentDirections.bookSelectTimeAction()
                binding.root.findNavController().navigate(action)
            }

        }

        binding.btnChangeBarber.setOnClickListener {
            val action = BookSelectServiceFragmentDirections.bookChangeBarberAction()
            binding.root.findNavController().navigate(action)
        }


    }
}