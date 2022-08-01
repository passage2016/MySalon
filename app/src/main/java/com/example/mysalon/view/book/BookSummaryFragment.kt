package com.example.mysalon.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.FragmentBookSummaryBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.view.book.adapter.ServiceItemAdapter
import com.example.mysalon.viewModel.MainViewModel

class BookSummaryFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentBookSummaryBinding
    lateinit var adapter: ServiceItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookSummaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        var date = mainViewModel.appointmentsDateLiveData.value!!
        val timeFrom = mainViewModel.appointmentsStartFromLiveData.value!!
        val slot = mainViewModel.appointmentsSlotLiveData.value!!
        val currentAppointments = mainViewModel.currentAppointmentsLiveData.value!!
        val totalDuration = mainViewModel.appointmentsTotalDurationLiveData.value!!
        val barbers = mainViewModel.barbersLiveData.value!!
        val barberId = mainViewModel.barberServicesIdLiveData.value!!
        val barber = barbers.get(barberId)
        val services = mainViewModel.barberServicesLiveData.value!!
        val selectedService = mainViewModel.barberServicesSelectLiveData.value!!
        binding.tvSelectedDayDate.text = date

        var fromTimeString = ""
        var toTimeString = ""
        currentAppointments.forEach() {
            if (it.date == date) {
                fromTimeString = it.slots.keys.elementAt(timeFrom).split("-")[0]
                toTimeString = it.slots.keys.elementAt(timeFrom + slot - 1).split("-")[1]
            }
        }
        var selectedTime = "$fromTimeString-$toTimeString ($totalDuration Minutes)"

        binding.tvSelectedTime.text = selectedTime
        binding.tvSelectedBarber.text = barber.barberName
        Glide.with(requireActivity().applicationContext)
            .load(Constants.BASE_IMAGE_URL + barber.profilePic)
            .into(binding.ivBarberPic)
        val selectedServiceList = services.filter { it.serviceId in selectedService }
        adapter = ServiceItemAdapter(this, selectedServiceList)
        binding.rvSelectedService.adapter = adapter
        binding.rvSelectedService.layoutManager = LinearLayoutManager(view.context)
        mainViewModel.setCouponCode("")
        binding.tvCouponCode.setOnClickListener {

        }
        var cost = 0.0
        selectedServiceList.forEach() {
            cost += it.cost
        }
        binding.tvTotalCost.text = "Total Cost: ${cost}"


        binding.btnContinue.setOnClickListener {
            val map = HashMap<String, Any>()
            map["userId"] = mainViewModel.userLiveData.value!!.userId
            map["barberId"] = barberId
            map["services"] = selectedService
            map["aptDate"] = date
            map["timeFrom"] = fromTimeString
            map["timeTo"] = toTimeString
            map["totalDuration"] = totalDuration
            map["totalCost"] = cost
            map["couponCode"] = mainViewModel.CouponCodeLiveData.value!!
            map["sendSms"] = false
            mainViewModel.bookAppointment(map)

        }
        val navController = binding.root.findNavController()
        mainViewModel.appointmentLiveData.observe(requireActivity()) {
            val action = BookSummaryFragmentDirections.bookConfirmAction()
            navController.navigate(action)
        }


        binding.btnCancel.setOnClickListener {
            val action = BookSummaryFragmentDirections.bookCancelAction()
            binding.root.findNavController().navigate(action)
        }
    }
}