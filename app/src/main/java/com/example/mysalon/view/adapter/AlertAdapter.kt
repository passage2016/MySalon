package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewAlertBinding
import com.example.mysalon.databinding.ViewProductBinding
import com.example.mysalon.databinding.ViewWorkingHourBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.alert.Alert
import com.example.mysalon.model.remote.data.getServicesByCategory.ServiceDetail
import com.example.mysalon.model.remote.data.product.Product
import com.example.mysalon.model.remote.data.workingHour.Weekday
import com.example.mysalon.view.fragment.ProductsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel


class AlertAdapter(private val fragment: Fragment, val infoList: ArrayList<Alert>) :
    RecyclerView.Adapter<AlertAdapter.AlertHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewAlertBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        binding =
            ViewAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return AlertHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class AlertHolder(val binding: ViewAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alert: Alert) {
            binding.tvMessage.text = alert.message
            binding.tvTime.text = alert.createdOn
        }
    }
}
