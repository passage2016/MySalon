package com.example.mysalon.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.R
import com.example.mysalon.databinding.FragmentBookRescheduleBinding
import com.example.mysalon.view.book.adapter.SelectDateAdapter
import com.example.mysalon.view.book.adapter.SelectTimeAdapter
import com.example.mysalon.viewModel.MainViewModel

class BookRescheduleFragment : Fragment() {
    lateinit var binding: FragmentBookRescheduleBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var dateAdapter: SelectDateAdapter
    lateinit var timeAdapter: SelectTimeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookRescheduleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.appointmentsStartFromLiveData.postValue(-1)
        mainViewModel.loadCurrentAppointments()

        binding.rvDates.visibility = View.GONE


        mainViewModel.currentAppointmentsLiveData.observe(requireActivity()) {
            val availableSlots = it.filter { it.slots.size > 0 }
            dateAdapter = SelectDateAdapter(this, availableSlots)
            binding.rvDates.adapter = dateAdapter
            binding.rvDates.layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            binding.tvSelectedDayDate.text = "${availableSlots[0].day}, ${availableSlots[0].date}"
            mainViewModel.appointmentsDateLiveData.postValue(availableSlots[0].date)
        }

        mainViewModel.appointmentsDateLiveData.observe(requireActivity()) { date ->
            mainViewModel.currentAppointmentsLiveData.value!!.forEach() {
                if (it.date == date) {
                    binding.tvSelectedDayDate.text = "${it.day}, ${it.date}"
                    timeAdapter = SelectTimeAdapter(this, it.slots)
                    binding.rvTimeSlots.adapter = timeAdapter
                    binding.rvTimeSlots.layoutManager = GridLayoutManager(view.context, 4)
                }
            }

        }

        binding.tvSelectSlots.text = "Select ${mainViewModel.appointmentsSlotLiveData.value} Slots"

        binding.idDropDown.isSelected = false
        binding.idDropDown.setOnClickListener {
            binding.idDropDown.isSelected = !binding.idDropDown.isSelected
            if (binding.idDropDown.isSelected) {
                binding.rvDates.visibility = View.VISIBLE
                binding.idDropDown.setImageResource(R.drawable.ic_up_arrow)
            } else {
                binding.rvDates.visibility = View.GONE
                binding.idDropDown.setImageResource(R.drawable.ic_down_arrow)
            }
        }

        binding.btnConfirm.setOnClickListener {
            if (mainViewModel.appointmentsStartFromLiveData.value == -1) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Time Error")
                    .setMessage("Please select time.")
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            } else {
                var fromTimeString = ""
                var toTimeString = ""
                val timeFrom = mainViewModel.appointmentsStartFromLiveData.value!!
                val slot = mainViewModel.appointmentsSlotLiveData.value!!
                val date = mainViewModel.appointmentsDateLiveData.value!!
                val currentAppointments = mainViewModel.currentAppointmentsLiveData.value!!
                currentAppointments.forEach() {
                    if (it.date == date) {
                        fromTimeString = it.slots.keys.elementAt(timeFrom).split("-")[0]
                        toTimeString = it.slots.keys.elementAt(timeFrom + slot - 1).split("-")[1]
                    }
                }
                val map = HashMap<String, Any>()
                map["aptNo"] = mainViewModel.appointmentLiveData.value!!.aptNo
                map["timeFrom"] = fromTimeString
                map["timeTo"] = toTimeString
                map["aptDate"] = date
                mainViewModel.rescheduleAppointment(map)
                val option = NavOptions.Builder().setPopUpTo(R.id.home_dest, false).build()
                val action =
                    BookRescheduleFragmentDirections.bookConfirmAction(mainViewModel.appointmentLiveData.value!!.aptNo)
                binding.root.findNavController().navigate(action, option)
            }
        }


        binding.btnCancel.setOnClickListener {
            val action =
                BookRescheduleFragmentDirections.bookCancelAction(mainViewModel.appointmentLiveData.value!!.aptNo)
            binding.root.findNavController().navigate(action)
        }
    }


}