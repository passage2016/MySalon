package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewServiceClassBinding
import com.example.mysalon.databinding.ViewShowCaseBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.model.remote.data.albumList.Album
import com.example.mysalon.model.remote.data.albumPhotos.Photo
import com.example.mysalon.view.ShowCaseClassFragmentDirections
import com.example.mysalon.viewModel.MainViewModel

class ShowCasePhotoAdapter(private val fragment: Fragment, val infoList: ArrayList<Photo>) :
    RecyclerView.Adapter<ShowCasePhotoAdapter.ShowCaseHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewShowCaseBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCaseHolder {
        binding =
            ViewShowCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return ShowCaseHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowCaseHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class ShowCaseHolder(val binding: ViewShowCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {

            Glide.with(fragment.requireActivity().applicationContext)
                .load(Constants.BASE_IMAGE_URL + photo.photoUrl)
                .into(binding.ivCategoryPhoto)

        }
    }
}
