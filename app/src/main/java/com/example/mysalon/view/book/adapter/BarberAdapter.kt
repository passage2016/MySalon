package com.example.mysalon.view.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewBookBarberSelectBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.getBarber.Barber
import com.example.mysalon.view.book.BookSelectBarberFragment
import com.example.mysalon.view.book.BookSelectBarberFragmentDirections


class BarberAdapter(private val context: Context, val infoArrayList: ArrayList<Barber>) :
    RecyclerView.Adapter<BarberAdapter.BarberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberHolder {
        val binding = ViewBookBarberSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BarberHolder(binding)
    }

    override fun onBindViewHolder(holder: BarberHolder, position: Int) {
        holder.apply {
            val info = infoArrayList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoArrayList.size
    }


    inner class BarberHolder(val binding: ViewBookBarberSelectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(barber: Barber){
            binding.tvBarberName.text = barber.barberName
            binding.rbRating.rating = barber.userRating.toFloat()
            binding.root.setOnClickListener {
                val action = BookSelectBarberFragmentDirections.bookSelectServiceAction(barber.barberId)
                binding.root.findNavController().navigate(action)
            }

            Glide.with(context)
                .load(BASE_IMAGE_URL + barber.profilePic)
                .into(binding.ivProfilePic)
        }
    }
}
