package com.example.mysalon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.databinding.ViewProductBinding
import com.example.mysalon.databinding.ViewReviewBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.getServicesByCategory.ServiceDetail
import com.example.mysalon.model.remote.data.product.Product
import com.example.mysalon.model.remote.data.review.getReview.Review
import com.example.mysalon.view.fragment.ProductsFragmentDirections
import com.example.mysalon.view.fragment.ReviewsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel


class ReviewAdapter(private val fragment: Fragment, val infoList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewReviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        binding =
            ViewReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return ReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class ReviewHolder(val binding: ViewReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.tvUserName.text = review.fullName
            binding.tvReviewDate.text = review.creationDate
            binding.tvDetail.text = review.comment
            binding.rbRating.rating = review.rating.toFloat()
            binding.root.setOnClickListener {
                mainViewModel.reviewLiveData.postValue(review)
                val action = ReviewsFragmentDirections.reviewDetailAction()
                binding.root.findNavController().navigate(action)

            }
            Glide.with(fragment)
                .load(review.profilePic)
                .into(binding.ivProfilePic)
        }
    }
}
