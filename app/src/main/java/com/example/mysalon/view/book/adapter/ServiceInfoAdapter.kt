package com.example.mysalon.view.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewBookServiceInfoBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.book.Service
import com.example.mysalon.view.book.BookInfoFragment
import com.example.mysalon.view.book.BookSummaryFragment
import com.example.mysalon.viewModel.MainViewModel


class ServiceInfoAdapter(private val fragment: BookInfoFragment, val infoList: List<Service>) :
    RecyclerView.Adapter<ServiceInfoAdapter.SelectServiceHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewBookServiceInfoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServiceHolder {
        binding =
            ViewBookServiceInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return SelectServiceHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectServiceHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class SelectServiceHolder(val binding: ViewBookServiceInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service) {
            binding.tvServiceName.text = service.serviceName
            binding.tvCost.text = service.cost.toString()
            binding.tvDuration.text = service.duration.toString()


            Glide.with(fragment.requireActivity().applicationContext)
                .load(BASE_IMAGE_URL + service.servicePic)
                .into(binding.ivServicePic)
        }
    }
}
