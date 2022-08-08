package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewProductBinding
import com.example.mysalon.databinding.ViewWorkingHourBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.getServicesByCategory.ServiceDetail
import com.example.mysalon.model.remote.data.product.Product
import com.example.mysalon.model.remote.data.workingHour.Weekday
import com.example.mysalon.view.fragment.ProductsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel


class WorkingHourAdapter(private val fragment: Fragment, val infoMap: Map<String, Weekday>) :
    RecyclerView.Adapter<WorkingHourAdapter.WorkingHourHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewWorkingHourBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingHourHolder {
        binding =
            ViewWorkingHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return WorkingHourHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkingHourHolder, position: Int) {
        holder.apply {
            val day = infoMap.keys.toList()[position]
            val weekday = infoMap.get(day)
            holder.bind(day, weekday!!)
            if (position == 0){
                binding.tvDay.setTextColor(fragment.getResources().getColor(R.color.teal_200))
                binding.tvTime.setTextColor(fragment.getResources().getColor(R.color.teal_200))
            }
        }
    }

    override fun getItemCount(): Int {
        return infoMap.size
    }


    inner class WorkingHourHolder(val binding: ViewWorkingHourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: String, weekday: Weekday) {
            var time = "${weekday.fromTime} to ${weekday.toTime}"
            if(weekday.fromTime == weekday.toTime){
                time = "Holiday"
                binding.tvTime.setTextColor(fragment.getResources().getColor(R.color.teal_200))
            }
            binding.tvDay.text = day
            binding.tvTime.text = time

        }
    }
}
