package com.example.mysalon.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewBookServiceInfoBinding
import com.example.mysalon.databinding.ViewServiceClassBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.model.remote.data.book.Service
import com.example.mysalon.model.remote.data.getServiceCategory.ServiceCategory
import com.example.mysalon.view.AppointmentsFragmentDirections
import com.example.mysalon.view.ServiceClassFragmentDirections
import com.example.mysalon.view.ServiceListFragmentDirections
import com.example.mysalon.viewModel.MainViewModel

class ServiceCategoriesAdapter(private val fragment: Fragment, val infoList: ArrayList<ServiceCategory>) :
    RecyclerView.Adapter<ServiceCategoriesAdapter.SelectServiceHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewServiceClassBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServiceHolder {
        binding =
            ViewServiceClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    inner class SelectServiceHolder(val binding: ViewServiceClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(serviceCategory: ServiceCategory) {
            binding.tvCategory.text = serviceCategory.category


            Glide.with(fragment.requireActivity().applicationContext)
                .load(Constants.BASE_IMAGE_URL + serviceCategory.categoryImage)
                .into(binding.ivCategoryPhoto)

            binding.root.setOnClickListener {
                mainViewModel.serviceCategoryIdLiveData.postValue(serviceCategory.categoryId)
                val action = ServiceClassFragmentDirections.servicesAction()
                binding.root.findNavController().navigate(action)
            }
        }
    }
}
