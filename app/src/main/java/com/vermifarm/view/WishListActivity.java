package com.vermifarm.view;

import static com.vermifarm.storage.LanguageUtils.loadLocale;
import static com.vermifarm.utils.Constant.PRODUCT;
import static com.vermifarm.utils.InternetUtils.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vermifarm.R;
import com.vermifarm.databinding.ActivityWishlistBinding;
import com.vermifarm.adapter.WishListAdapter;
import com.vermifarm.model.Product;
import com.vermifarm.storage.LoginUtils;
import com.vermifarm.viewmodel.FavoriteViewModel;

import java.util.List;

public class WishListActivity extends AppCompatActivity {

    private ActivityWishlistBinding binding;
    private WishListAdapter wishListAdapter;
    private List<Product> favoriteList;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wishlist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.my_wishList));

        setUpRecyclerView();

        getFavorites();
    }

    private void setUpRecyclerView() {
        binding.favoriteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.favoriteList.setHasFixedSize(true);
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
    }

    private void getFavorites() {
        if (isNetworkConnected(this)) {
            favoriteViewModel.getFavorites(LoginUtils.getInstance(this).getUserInfo().getId()).observe(this, favoriteApiResponse -> {
                if (favoriteApiResponse != null) {
                    favoriteList = favoriteApiResponse.getFavorites();
                    if (favoriteList.size() == 0) {
                        binding.noBookmarks.setVisibility(View.VISIBLE);
                        binding.emptyWishlist.setVisibility(View.VISIBLE);
                    }else {
                        binding.favoriteList.setVisibility(View.VISIBLE);
                    }
                    wishListAdapter = new WishListAdapter(getApplicationContext(), favoriteList, product -> {
                        Intent intent = new Intent(WishListActivity.this, DetailsActivity.class);
                        // Pass an object of product class
                        intent.putExtra(PRODUCT, (product));
                        startActivity(intent);
                    },this);
                }
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.favoriteList.setAdapter(wishListAdapter);
            });
        }else {
            binding.emptyWishlist.setVisibility(View.VISIBLE);
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.emptyWishlist.setText(getString(R.string.no_internet_connection));
        }
    }

}
