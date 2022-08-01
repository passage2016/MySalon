package com.example.mysalon.view.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysalon.databinding.ViewBookServiceClassBinding
import com.example.mysalon.view.book.BookSelectServiceFragment
import com.example.mysalon.viewModel.MainViewModel


class SelectServiceClassAdapter(
    private val fragment: BookSelectServiceFragment, val serviceTypeArrayList: ArrayList<String>
) :
    RecyclerView.Adapter<SelectServiceClassAdapter.SelectServiceHolder>() {
    lateinit var adapter: SelectServiceAdapter
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewBookServiceClassBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServiceHolder {
        binding =
            ViewBookServiceClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]

        return SelectServiceHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectServiceHolder, position: Int) {
        holder.apply {
            val info = serviceTypeArrayList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return serviceTypeArrayList.size
    }


    inner class SelectServiceHolder(val binding: ViewBookServiceClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(serviceType: String) {
            binding.tvServiceClass.text = serviceType
            adapter = SelectServiceAdapter(
                fragment,
                mainViewModel.barberServicesLiveData.value!!.filter { it.serviceType == serviceType })
            binding.rvServices.adapter = adapter
            binding.rvServices.layoutManager = LinearLayoutManager(fragment.requireActivity())
        }
    }
}
