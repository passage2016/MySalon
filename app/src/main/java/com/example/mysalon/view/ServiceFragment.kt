package com.example.mysalon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysalon.databinding.FragmentBookInfoBinding

class ServiceFragment : Fragment() {
    lateinit var binding: FragmentBookInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        this.rvCategory.addItemDecoration((RecyclerView.ItemDecoration)new GridSpacingItemDecoration(2, 30, true));
//    this.rvCategory.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager(this.context, 2));

    }
}