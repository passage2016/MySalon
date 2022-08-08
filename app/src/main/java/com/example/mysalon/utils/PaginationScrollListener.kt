package com.example.mysalon.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationScrollListener: RecyclerView.OnScrollListener {
    var layoutManager: LinearLayoutManager
    var pageTool: PageTool

    constructor(layoutManager: LinearLayoutManager?, pageTool: PageTool) {
        this.layoutManager = layoutManager!!
        this.pageTool = pageTool
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.getChildCount()
        val totalItemCount: Int = layoutManager.getItemCount()
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()

    abstract fun getTotalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}