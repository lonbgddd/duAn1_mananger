package com.example.duan1_mananger.table.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.databinding.ItemOderMenuBinding;
import com.example.duan1_mananger.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OderAdapter extends RecyclerView.Adapter<OderAdapter.ViewHolder>{
    List<Product> productList;


    public OderAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOderMenuBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null){
            return;
        } else
            holder.initData(product);
    }

    @Override
    public int getItemCount() {
        if (productList != null){
            return productList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvPrice, tvCountProduct;
        private ImageView icAscending,icDecrease;
        public ViewHolder(@NonNull ItemOderMenuBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvNameProductOder;
            tvPrice = binding.tvMoneyProductOder;
            icAscending = binding.icAscending;
            icDecrease = binding.icDecrease;
            tvCountProduct = binding.tvCountProduct;
        }
         public void initData(Product product){
            tvName.setText(product.getNameProduct());
            Locale locale = new Locale("en","EN");
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
             Double price = product.getPrice();
             String strPrice = numberFormat.format(price);
             tvPrice.setText(strPrice);
            icAscending.setOnClickListener(ic ->{
                Double priceChange = product.getPrice();
                int count= Integer.parseInt(String.valueOf(tvCountProduct.getText()));
                count++;
                priceChange *= count;
                tvCountProduct.setText(String.valueOf(count));
                String strPriceChange = numberFormat.format(priceChange);
                tvPrice.setText(strPriceChange);
            });
            icDecrease.setOnClickListener(ic ->{
                Double priceChange = product.getPrice();
                int count= Integer.parseInt(String.valueOf(tvCountProduct.getText()));
                if(count > 1){
                    count--;
                    priceChange *= count;
                    tvCountProduct.setText(String.valueOf(count));
                    String strPriceChange = numberFormat.format(priceChange);
                    tvPrice.setText(strPriceChange);
                }
            });
        }




    }
}
