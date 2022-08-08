package com.example.mysalon.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysalon.databinding.FragmentServiceBinding
import com.example.mysalon.utils.PageTool
import com.example.mysalon.utils.PaginationScrollListener
import com.example.mysalon.view.adapter.ProductAdapter
import com.example.mysalon.viewModel.MainViewModel


class ProductsFragment : Fragment() {
    lateinit var binding: FragmentServiceBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if(mainViewModel.productPageLiveData.value == null){
            mainViewModel.loadProducts()
        }




        mainViewModel.productsListLiveData.observe(requireActivity()) {
            adapter = ProductAdapter(this, it)
            binding.rvServices.adapter = adapter
            val linearLayoutManager = LinearLayoutManager(this.context)
            binding.rvServices.layoutManager = linearLayoutManager
            binding.rvServices.addOnScrollListener(
                ProductPaginationScrollListener(
                    linearLayoutManager,
                    mainViewModel.productPageLiveData.value!!
                )
            )
        }

    }

    inner class ProductPaginationScrollListener(
        layoutManager: LinearLayoutManager,
        pageTool: PageTool
    ) : PaginationScrollListener(layoutManager, pageTool) {

        override fun loadMoreItems() {
            pageTool.isLoading = true
            pageTool.currentPage += 1
            mainViewModel.loadProducts()
        }

        override fun getTotalPageCount(): Int {
            return pageTool.totalPage
        }

        override fun isLastPage(): Boolean {
            return pageTool.isLastPage
        }

        override fun isLoading(): Boolean {

            return pageTool.isLoading
        }


    }
}