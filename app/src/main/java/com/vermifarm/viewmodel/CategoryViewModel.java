package com.vermifarm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vermifarm.model.Product;
import com.vermifarm.net.ProductDataSource;
import com.vermifarm.net.ProductDataSourceFactory;

public class CategoryViewModel extends ViewModel {

    // Create liveData for PagedList and PagedKeyedDataSource
    public LiveData<PagedList<Product>> categoryPagedList;

    public void loadProductsByCategory(String category, int userId) {
        // Get our database source factory
        ProductDataSourceFactory productDataSourceFactory = new ProductDataSourceFactory(category,userId);

        // Get PagedList configuration
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ProductDataSource.PAGE_SIZE)
                        .build();

        // Build the paged list
        categoryPagedList = (new LivePagedListBuilder<>(productDataSourceFactory, pagedListConfig)).build();
    }
}
