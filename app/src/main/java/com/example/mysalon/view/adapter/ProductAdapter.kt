package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewProductBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.getServicesByCategory.ServiceDetail
import com.example.mysalon.model.remote.data.product.Product
import com.example.mysalon.view.fragment.ProductsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel


class ProductAdapter(private val fragment: Fragment, val infoList: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.SelectServiceHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServiceHolder {
        binding =
            ViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    inner class SelectServiceHolder(val binding: ViewProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.productName
            binding.tvCost.text = product.price.toString()
            binding.root.setOnClickListener {
                mainViewModel.productLiveData.postValue(product)
                val action = ProductsFragmentDirections.productDetailAction()
                binding.root.findNavController().navigate(action)

            }


            Glide.with(fragment)
                .load(BASE_IMAGE_URL + product.productPic)
                .into(binding.ivServicePic)
        }
    }
}
