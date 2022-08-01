package com.example.mysalon.view.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewBookSelectDateBinding
import com.example.mysalon.model.remote.data.currentAppointments.Slot
import com.example.mysalon.view.book.BookSelectTimeFragment
import com.example.mysalon.viewModel.MainViewModel


class SelectDateAdapter(private val fragment: Fragment, val infoList: List<Slot>) :
    RecyclerView.Adapter<SelectDateAdapter.SelectDateHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewBookSelectDateBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDateHolder {
        binding =
            ViewBookSelectDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return SelectDateHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectDateHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class SelectDateHolder(val binding: ViewBookSelectDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(slot: Slot) {
            binding.tvDate.text = slot.date
            binding.tvDay.text = slot.day
            mainViewModel.appointmentsDateLiveData.observe(fragment.requireActivity()) {
                if (slot.date == it) {
                    binding.llWrapper.setBackgroundColor(
                        ContextCompat.getColor(
                            fragment.requireContext(),
                            R.color.teal_200
                        )
                    )
                } else {
                    binding.llWrapper.setBackgroundColor(
                        ContextCompat.getColor(
                            fragment.requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            binding.root.setOnClickListener {
                mainViewModel.setAppointmentsDate(slot.date)
                mainViewModel.setAppointmentsStartFrom(-1)
            }
        }
    }
}
