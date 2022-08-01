package com.example.mysalon.view.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewBookSelectTimeBinding
import com.example.mysalon.view.book.BookSelectTimeFragment
import com.example.mysalon.viewModel.MainViewModel


class SelectTimeAdapter(
    private val fragment: Fragment,
    val infoMap: Map<String, Boolean>
) :
    RecyclerView.Adapter<SelectTimeAdapter.SelectDateHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewBookSelectTimeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDateHolder {
        binding =
            ViewBookSelectTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return SelectDateHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectDateHolder, position: Int) {
        holder.apply {
            val info = infoMap.keys.elementAt(position)
            holder.bind(info, infoMap[info]!!, position)
        }
    }

    override fun getItemCount(): Int {
        return infoMap.size
    }

    fun freeSlots(slots: Int, position: Int): Int {
        for (i in 0 until slots) {
            if (position + i >= infoMap.size || infoMap[infoMap.keys.elementAt(position + i)] == true) {
                return i
            }
        }

        return -1
    }


    inner class SelectDateHolder(val binding: ViewBookSelectTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(time: String, booked: Boolean, position: Int) {
            binding.tvTimeSlot.text = time.split("-")[0]
            if (booked) {
                binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_booked)
            } else {
                binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_available)
            }
            binding.tvTimeSlot.setOnClickListener {
                val slots = mainViewModel.appointmentsSlotLiveData.value!!
                val freeSlots = freeSlots(slots, position)
                if (freeSlots == -1) {
                    mainViewModel.setAppointmentsStartFrom(position)
                } else {
                    val builder = AlertDialog.Builder(fragment.requireContext())
                        .setTitle("Login Error")
                        .setMessage("Total required slots are $slots. From current selected position only $freeSlots are available.")
                        .setPositiveButton("Ok") { _, _ ->
                        }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()
                }
            }

            mainViewModel.appointmentsStartFromLiveData.observe(fragment.requireActivity()) {
                val slots = mainViewModel.appointmentsSlotLiveData.value!!
                if (it != -1 && position in it until (it + slots)) {
                    binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_selected)
                } else {
                    if (booked) {
                        binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_booked)
                    } else {
                        binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_available)
                    }
                }
            }

        }
    }
}
