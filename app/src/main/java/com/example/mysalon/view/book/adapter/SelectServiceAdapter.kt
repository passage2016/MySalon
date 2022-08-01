package com.example.mysalon.view.book.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewBookServiceItemBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.getBarberService.Service
import com.example.mysalon.view.book.BookSelectServiceFragment
import com.example.mysalon.viewModel.MainViewModel


class SelectServiceAdapter(private val fragment: BookSelectServiceFragment, val infoList: List<Service>) :
    RecyclerView.Adapter<SelectServiceAdapter.SelectServiceHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewBookServiceItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServiceHolder {
        binding =
            ViewBookServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    inner class SelectServiceHolder(val binding: ViewBookServiceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service) {
            binding.tvServiceName.text = service.serviceName
            binding.tvCost.text = service.cost.toString()
            binding.tvDuration.text = service.duration.toString()
            if(service.serviceId in mainViewModel.barberServicesSelectLiveData.value!!){
                binding.ivSelect.isSelected = true
            }
            binding.root.setOnClickListener {
                binding.ivSelect.isSelected = !binding.ivSelect.isSelected
                if (binding.ivSelect.isSelected && !mainViewModel.barberServicesSelectLiveData.value!!.contains(
                        service.serviceId
                    )
                ) {
                    mainViewModel.barberServicesSelectLiveData.value!!.add(service.serviceId)
                }

                if (!binding.ivSelect.isSelected && mainViewModel.barberServicesSelectLiveData.value!!.contains(
                        service.serviceId
                    )
                ) {
                    mainViewModel.barberServicesSelectLiveData.value!!.remove(service.serviceId)
                }
                Log.e(
                    "barberServicesSelect",
                    mainViewModel.barberServicesSelectLiveData.value.toString()
                )
            }

            Glide.with(fragment.requireActivity().applicationContext)
                .load(BASE_IMAGE_URL + service.servicePic)
                .into(binding.ivServicePic)
        }
    }
}
