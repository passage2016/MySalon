package com.example.mysalon.view.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.FragmentBookInfoBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.utils.QRCodeUtils
import com.example.mysalon.view.book.adapter.ServiceInfoAdapter
import com.example.mysalon.viewModel.MainViewModel

class BookInfoFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentBookInfoBinding
    lateinit var adapter: ServiceInfoAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        var appointment = mainViewModel.appointmentLiveData.value!!
        //open appointment
        mainViewModel.barberServicesIdLiveData.postValue(appointment.barberId)

        binding.tvSelectedDayDate.text = appointment.aptDate
        binding.tvSelectedTime.text =
            "${appointment.timeFrom} to ${appointment.timeTo} (${appointment.totalDuration} Minutes) - ${appointment.aptStatus}"
        binding.tvSelectedBarber.text = appointment.barberName
        Glide.with(requireActivity().applicationContext)
            .load(Constants.BASE_IMAGE_URL + appointment.profilePic)
            .into(binding.ivBarberPic)
        adapter = ServiceInfoAdapter(this, appointment.services)
        binding.rvSelected.adapter = adapter
        binding.rvSelected.layoutManager = LinearLayoutManager(view.context)
        binding.tvAptNo.text = appointment.aptNo.toString()
        val qrCode = QRCodeUtils().createQRCode(appointment.aptNo.toString())
        Glide.with(requireActivity().applicationContext)
            .load(qrCode)
            .into(binding.ivQrCode)
        if(appointment.aptStatus == "Confirmed"){
            Glide.with(requireActivity().applicationContext)
                .load(R.drawable.confirmed)
                .into(binding.ivStamp)
        } else {
            Glide.with(requireActivity().applicationContext)
                .load(R.drawable.canceled)
                .into(binding.ivStamp)
        }


        binding.btnCancel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Confirm Cancel")
                .setMessage("Are your sure you want to cancel this appointment? Once cancelled, you will be no more able to claim for this appointment.")
                .setPositiveButton("YES") { _, _ ->
                    mainViewModel.cancelAppointment()
                }
                .setNegativeButton("No"){_, _->}
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
        binding.btnReschedule.setOnClickListener {
            val action = BookInfoFragmentDirections.bookRescheduleAction()
            binding.root.findNavController().navigate(action)
        }

        mainViewModel.appointmentLiveData.observe(requireActivity()){
            appointment = mainViewModel.appointmentLiveData.value!!
            binding.tvSelectedTime.text =
                "${appointment.timeFrom} to ${appointment.timeTo} (${appointment.totalDuration} Minutes) - ${appointment.aptStatus}"
            Glide.with(requireActivity().applicationContext)
                .load(R.drawable.canceled)
                .into(binding.ivStamp)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            Log.e("back", "back")
            val action = BookInfoFragmentDirections.bookExitAction()
            binding.root.findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)

    }
}