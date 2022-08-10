package com.example.mysalon.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mysalon.R
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
        mainViewModel.getCoupons()

        var date = mainViewModel.appointmentsDateLiveData.value!!
        val timeFrom = mainViewModel.appointmentsStartFromLiveData.value!!
        val slot = mainViewModel.appointmentsSlotLiveData.value!!
        val currentAppointments = mainViewModel.currentAppointmentsLiveData.value!!
        val totalDuration = mainViewModel.appointmentsTotalDurationLiveData.value!!
        val barbers = mainViewModel.barbersLiveData.value!!
        val barberId = mainViewModel.barberServicesIdLiveData.value!!
        val barber = barbers.filter { it.barberId == barberId }.get(0)
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
        mainViewModel.couponCodeLiveData.postValue("")
        binding.tvCouponCode.setOnClickListener {

            val list = mainViewModel.couponLiveData.value!!
            var coupons = arrayOfNulls<String>(list.size)
            list.toArray(coupons)

            val builder = AlertDialog.Builder(this.requireContext())
                .setTitle("Select Coupon")
                .setSingleChoiceItems(coupons, -1){
                        dialog, position ->
                    mainViewModel.couponCodeLiveData.postValue(coupons[position])
                    binding.tvCouponCode.text = "Coupon: " + coupons[position]
                    dialog.dismiss()
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
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
            map["couponCode"] = mainViewModel.couponCodeLiveData.value!!
            map["sendSms"] = false
            mainViewModel.bookAppointment(map)
            val action = BookSummaryFragmentDirections.bookConfirmAction(-1)
            val option = NavOptions.Builder().setPopUpTo(R.id.home_dest, false).build()
            binding.root.findNavController().navigate(action, option)

        }



        binding.btnCancel.setOnClickListener {
            val action = BookSummaryFragmentDirections.bookCancelAction()
            binding.root.findNavController().navigate(action)
        }
    }
}